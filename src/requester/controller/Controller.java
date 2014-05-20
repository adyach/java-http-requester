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
 * Controller.
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

    protected void setModelProperty(String propertyName, Object newValue) {

        for (Model model: registeredModels) {

            log.debug("model : " + model);

            try {
                Method method = model.getClass().getMethod("set" + propertyName, new Class[] {newValue.getClass()});
                method.invoke(model, newValue);
            } catch (Exception ex) {
                log.debug("No such method: set" + propertyName + "(" + newValue.getClass() + ")");
                //                log.warn("Known methods:");
                //                for (Method method: model.getClass().getDeclaredMethods()) {
                //                    log.warn(method.getName());
                //                }
            }
        }
    }

}
