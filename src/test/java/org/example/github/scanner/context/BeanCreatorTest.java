package org.example.github.scanner.context;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
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

        Throwable throwable = Assertions.assertThrows(BeanCreator.CreationBeanException.class,
                () -> subj.createObjectWithoutDependencies(constructors[0]));


        Assertions.assertNotNull(throwable);

        String expectedErrorMessage = String.format(BeanCreator.CreationBeanException.PATTERN, TestWithDependencies.class.getCanonicalName());
        Assertions.assertEquals(expectedErrorMessage, throwable.getMessage());
    }

    @Test
    void createObjectWithoutDependencies_whenNoArgConstructorIsPresent_thenCreateObject() {
        Constructor<?>[] constructors = TestWithNoArgConstructor.class.getDeclaredConstructors();

        Object objectWithoutDependencies = subj.createObjectWithoutDependencies(constructors[0]);

        Assertions.assertNotNull(objectWithoutDependencies);
        Assertions.assertTrue(objectWithoutDependencies instanceof TestWithNoArgConstructor);
    }


    @Test
    void createAndGet_whenNoArgConstructorIsPresent_thenCreateObject() {
        Map<String, Object> context = new HashMap<>();
        TestWithNoArgConstructor createdObject = subj.createAndGet(TestWithNoArgConstructor.class, context);

        Assertions.assertNotNull(createdObject);
        Object saveToContextObject = context.get(TestWithNoArgConstructor.class.getCanonicalName());

        Assertions.assertNotNull(saveToContextObject);
        Assertions.assertTrue(saveToContextObject instanceof TestWithNoArgConstructor);
        Assertions.assertSame(saveToContextObject, createdObject);
    }

    @Test
    void createAndGet_whenObjectWithDependencies_thenCreateAllAndReturnCalledOne() {
        Map<String, Object> context = new HashMap<>();
        TestWithDependencies createdObject = subj.createAndGet(TestWithDependencies.class, context);

        Assertions.assertNotNull(createdObject);
        Assertions.assertNotNull(createdObject.getSubj());
    }

    @Test
    void createAndGet_whenObjectWithDependenciesAndDependencyExists_thenCreateAndReturn() {
        Map<String, Object> context = new HashMap<>();
        TestWithNoArgConstructor obj = subj.createAndGet(TestWithNoArgConstructor.class, context);
        TestWithDependencies createdObject = subj.createAndGet(TestWithDependencies.class, context);

        Assertions.assertNotNull(createdObject);
        Assertions.assertNotNull(createdObject.getSubj());
        Assertions.assertSame(obj, createdObject.getSubj());
    }

    @Test
    void createAndGet_whenObjectWithTwoLvlDependencies_thenCreateAllAndReturnCalledOne() {
        Map<String, Object> context = new HashMap<>();
        TestWithSecondLvlDependencies createdObject = subj.createAndGet(TestWithSecondLvlDependencies.class, context);

        Assertions.assertNotNull(createdObject);
        Assertions.assertNotNull(createdObject.getSubj());
    }

    @Test
    void createAndGet_whenObjectWithLoopedDependencies_thenThrowException() {
        Map<String, Object> context = new HashMap<>();
        Throwable exception = Assertions.assertThrows(BeanCreator.CreationBeanLoopException.class,
                () -> subj.createAndGet(TestWithLoopedDependencies.class, context));

        String expectedMsg = String.format(BeanCreator.CreationBeanLoopException.PATTERN, TestWithLoopedDependencies.class.getCanonicalName());
        Assertions.assertEquals(expectedMsg, exception.getMessage());
    }

    @Test
    void createObjectWithDependencies_whenNotMatchingArgsForConstructor_thenThrowException() {
        Constructor<?>[] constructors = TestWithDependencies.class.getConstructors();


        Throwable exception = Assertions.assertThrows(BeanCreator.CreationBeanException.class,
                () -> subj.createObjectWithDependencies(constructors[0], Collections.singletonList("123")));

        String expectedMsg = String.format(BeanCreator.CreationBeanException.PATTERN, TestWithDependencies.class.getCanonicalName());
        Assertions.assertEquals(expectedMsg, exception.getMessage());
    }


}