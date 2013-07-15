package logic.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * 
 * @author Andrey Dyachkov
 * Created on 15.07.2013
 */
public abstract class Model
{

    protected PropertyChangeSupport propertyChangeSupport;

    public Model() {

        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {

        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {

        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {

        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

}
