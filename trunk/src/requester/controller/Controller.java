package requester.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import requester.logic.model.Model;
import requester.view.View;

/**
 * 
 * @author Andrey Dyachkov
 * Created on 15.07.2013
 */

public abstract class Controller implements PropertyChangeListener {

    private static final Logger log = Logger.getLogger(Controller.class);

    private final List<View> registeredViews;
    private final List<Model> registeredModels;

    public Controller() {

        registeredViews = new ArrayList<View>();
        registeredModels = new ArrayList<Model>();
    }

    public void addModel(Model model) {

        registeredModels.add(model);
        model.addPropertyChangeListener(this);
    }

    public void removeModel(Model model) {

        registeredModels.remove(model);
        model.removePropertyChangeListener(this);
    }

    public void addView(View view) {

        registeredViews.add(view);
    }

    public void removeView(View view) {

        registeredViews.remove(view);
    }

    //  Use this to observe property changes from registered models
    //  and propagate them on to all the views.
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        for (View view: registeredViews) {
            view.modelPropertyChange(evt);
        }
    }

    /**
     * This is a convenience method that subclasses can call upon
     * to fire property changes back to the models. This method
     * uses reflection to inspect each of the model classes
     * to determine whether it is the owner of the property
     * in question. If it isn't, a NoSuchMethodException is thrown,
     * which the method ignores.
     *
     * @param propertyName = The name of the property.
     * @param newValue = An object that represents the new value
     * of the property.
     */
    protected void setModelProperty(String propertyName, Object newValue) {

        for (Model model: registeredModels) {
            try {
                Method method = model.getClass().getMethod("set" + propertyName, new Class[] {newValue.getClass()});
                method.invoke(model, newValue);
            } catch (Exception ex) {
                log.info("No such method: set" + propertyName + "(" + newValue.getClass() + ")");
                //                log.warn("Known methods:");
                //                for (Method method: model.getClass().getDeclaredMethods()) {
                //                    log.warn(method.getName());
                //                }
            }
        }
    }

}
