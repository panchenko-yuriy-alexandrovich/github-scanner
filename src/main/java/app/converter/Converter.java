package app.converter;

public interface Converter <F, T> {
    T convert(F from) throws Exception;
}
