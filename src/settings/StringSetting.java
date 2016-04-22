package settings;

/**
 *
 * @author Darren Eidson
 */
public class StringSetting extends Settings<String> {

    public StringSetting(String value) {
        super(value);
    }

    @Override
    public String getValue() {
        return settingValue;
    }

    @Override
    public void setValue(String value) {
        settingValue = value;
    }
    
}
