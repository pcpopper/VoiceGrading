package settings;

/**
 *
 * @author Darren Eidson
 * @param <T> Type
 */
public abstract class Settings<T> {
    
    protected T settingValue;
    
    public Settings() {}
    
    public Settings(T value) {
        setValue(value);
    }
    
    public abstract T getValue();
    
    public abstract void setValue(T value);
}
