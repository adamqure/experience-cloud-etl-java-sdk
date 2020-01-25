package ParameterClasses.Abstracts;

public abstract class DataSetIdInterface
{
    String identifier;

    public DataSetIdInterface(String id)
    {
        this.identifier = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
