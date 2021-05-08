package app.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class BeanCreatorTest {

    BeanCreator subj = new BeanCreator();

    public static class TestWithNoArgConstructor {
        private TestWithNoArgConstructor() {

        }
    }

    public static class TestWithDependencies {
        private TestWithNoArgConstructor subj;

        public TestWithDependencies(TestWithNoArgConstructor subj) {
            this.subj = subj;
        }

        public TestWithNoArgConstructor getSubj() {
            return subj;
        }
    }

    public static class TestWithSecondLvlDependencies {
        private TestWithDependencies subj;

        public TestWithSecondLvlDependencies(TestWithDependencies subj) {
            this.subj = subj;
        }

        public TestWithDependencies getSubj() {
            return subj;
        }
    }

    public static class TestWithLoopedDependencies {
        private TestWithNoArgConstructor testWithNoArgConstructor;
        private TestWithDependenciesForLoop loopedClass;

        public TestWithLoopedDependencies(TestWithNoArgConstructor testWithNoArgConstructor, TestWithDependenciesForLoop loopedClass) {
            this.testWithNoArgConstructor = testWithNoArgConstructor;
            this.loopedClass = loopedClass;
        }
    }

    public static class TestWithDependenciesForLoop {
        private TestWithLoopedDependencies loopedClass;

        public TestWithDependenciesForLoop(TestWithLoopedDependencies loopedClass) {
            this.loopedClass = loopedClass;
        }
    }

    @Test
    void createObjectWithoutDependencies_whenNoArgConstructorIsAbsent_thenThrowCreationBeanException() {
        Constructor<?>[] constructors = TestWithDependencies.class.getDeclaredConstructors();

        Throwable throwable = assertThrows(BeanCreator.CreationBeanException.class,
                () -> subj.createObjectWithoutDependencies(constructors[0]));


        assertNotNull(throwable);

        String expectedErrorMessage = String.format(BeanCreator.CreationBeanException.PATTERN, TestWithDependencies.class.getCanonicalName());
        assertEquals(expectedErrorMessage, throwable.getMessage());
    }

    @Test
    void createObjectWithoutDependencies_whenNoArgConstructorIsPresent_thenCreateObject() {
        Constructor<?>[] constructors = TestWithNoArgConstructor.class.getDeclaredConstructors();

        Object objectWithoutDependencies = subj.createObjectWithoutDependencies(constructors[0]);

        assertNotNull(objectWithoutDependencies);
        assertTrue(objectWithoutDependencies instanceof TestWithNoArgConstructor);
    }


    @Test
    void createAndGet_whenNoArgConstructorIsPresent_thenCreateObject() {
        Map<String, Object> context = new HashMap<>();
        TestWithNoArgConstructor createdObject = subj.createAndGet(TestWithNoArgConstructor.class, context);

        assertNotNull(createdObject);
        Object saveToContextObject = context.get(TestWithNoArgConstructor.class.getCanonicalName());

        assertNotNull(saveToContextObject);
        assertTrue(saveToContextObject instanceof TestWithNoArgConstructor);
        assertSame(saveToContextObject, createdObject);
    }

    @Test
    void createAndGet_whenObjectWithDependencies_thenCreateAllAndReturnCalledOne() {
        Map<String, Object> context = new HashMap<>();
        TestWithDependencies createdObject = subj.createAndGet(TestWithDependencies.class, context);

        assertNotNull(createdObject);
        assertNotNull(createdObject.getSubj());
    }

    @Test
    void createAndGet_whenObjectWithDependenciesAndDependencyExists_thenCreateAndReturn() {
        Map<String, Object> context = new HashMap<>();
        TestWithNoArgConstructor obj = subj.createAndGet(TestWithNoArgConstructor.class, context);
        TestWithDependencies createdObject = subj.createAndGet(TestWithDependencies.class, context);

        assertNotNull(createdObject);
        assertNotNull(createdObject.getSubj());
        assertSame(obj, createdObject.getSubj());
    }

    @Test
    void createAndGet_whenObjectWithTwoLvlDependencies_thenCreateAllAndReturnCalledOne() {
        Map<String, Object> context = new HashMap<>();
        TestWithSecondLvlDependencies createdObject = subj.createAndGet(TestWithSecondLvlDependencies.class, context);

        assertNotNull(createdObject);
        assertNotNull(createdObject.getSubj());
    }

    @Test
    void createAndGet_whenObjectWithLoopedDependencies_thenThrowException() {
        Map<String, Object> context = new HashMap<>();
        Throwable exception = assertThrows(BeanCreator.CreationBeanLoopException.class,
                () -> subj.createAndGet(TestWithLoopedDependencies.class, context));

        String expectedMsg = String.format(BeanCreator.CreationBeanLoopException.PATTERN, TestWithLoopedDependencies.class.getCanonicalName());
        assertEquals(expectedMsg, exception.getMessage());
    }

    @Test
    void createObjectWithDependencies_whenNotMatchingArgsForConstructor_thenThrowException() {
        Constructor<?>[] constructors = TestWithDependencies.class.getConstructors();


        Throwable exception = assertThrows(BeanCreator.CreationBeanException.class,
                () -> subj.createObjectWithDependencies(constructors[0], Collections.singletonList("123")));

        String expectedMsg = String.format(BeanCreator.CreationBeanException.PATTERN, TestWithDependencies.class.getCanonicalName());
        assertEquals(expectedMsg, exception.getMessage());
    }

    @Test
    void createWithoutDependencies_whenDependencyIsInContext_thenReturnFromContext() throws NoSuchMethodException {
        Date expectedDate = new Date();
        Map<String, Object> context = new HashMap<>();
        context.put(Date.class.getCanonicalName(), expectedDate);

        Object returnedDate = subj.getOrCreateWithoutDependencies(Date.class.getConstructor(), context);
        assertSame(expectedDate, returnedDate);
    }

}