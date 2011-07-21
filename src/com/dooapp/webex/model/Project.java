package com.dooapp.webex.model;

import com.dooapp.fxform.model.EnumProperty;
import javafx.beans.property.StringProperty;

/**.
 * User: dooApp
 * Date: 20/07/11
 * Time: 15:47
 */
public class Project {

    private StringProperty name = new StringProperty();

    private StringProperty description = new StringProperty();

    private EnumProperty<ProjectType> type = new EnumProperty<ProjectType>(ProjectType.class);

    public Project(String name) {
         setName(name);
    }

    public final String getName() {
        return name.get();
    }

    public final void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
