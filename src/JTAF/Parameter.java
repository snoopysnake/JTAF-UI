package JTAF;

/**
 * Created by Michael on 06/28/2017.
 */
public class Parameter {
    private String parameterName;
    private String parameterTag;
    private String parameterText;
    public Parameter(String name, String tag) {
        setName(name);
        setTag(tag);
        setText("");
    }
    public Parameter(String name, String tag,String text) {
        setName(name);
        setTag(tag);
        setText(text);
    }

    private void setName(String name) {
        this.parameterName = name;
    }
    private void setTag(String tag) {
        this.parameterTag = tag;
    }
    private void setText(String text) {
        this.parameterText = text;
    }
    public String getName() {
        return parameterName;
    }
    public String getTag() {
        return parameterTag;
    }
    public String getText() {
        return parameterText;
    }

    public String formatted() {
        return "Name:\t"+getName()+"\n"+
                "Tag:\t\t"+getTag()+"\n"+
                "Text:\t\t"+getText();
    }
}
