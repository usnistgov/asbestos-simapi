package gov.nist.asbestos.simapi.tk.simCommon


import groovy.transform.TypeChecked
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 *
 */
@TypeChecked
public class RuntimeManager {
    static Logger logger = Logger.getLogger(RuntimeManager.class);

    public static BaseActorSimulator getSimulatorRuntime(SimId simId) throws Exception, IOException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        SimulatorConfig config = GenericSimulatorFactory.getSimConfig(simId);
        String actorTypeName = config.getActorType();
        gov.nist.asbestos.simapi.tk.actors.ActorType actorType = gov.nist.asbestos.simapi.tk.actors.ActorType.findActor(actorTypeName);
        String actorSimClassName = actorType.getSimulatorClassName();
        if (StringUtils.isBlank(actorSimClassName)) return null;
        logger.info("Loading runtime for proxy " + simId + " of type " + actorTypeName + " of class " + actorSimClassName);
        Class<?> clas = Class.forName(actorSimClassName);

        // find correct constructor - no parameters
        Constructor<?>[] constructors = clas.getConstructors();
        Constructor<?> constructor = null;
        for (int i=0; i<constructors.length; i++) {
            Constructor<?> cons = constructors[i];
            Class<?>[] parmTypes = cons.getParameterTypes();
            if (parmTypes.length != 0) continue;
//				if (!parmTypes[0].getSimpleName().equals(dsSimCommon.getClass().getSimpleName())) continue;
//				if (!parmTypes[1].getSimpleName().equals(asc.getClass().getSimpleName())) continue;
            constructor = cons;
        }
        assert constructor : "Cannot find no-argument constructor for " + actorSimClassName
        Object obj = constructor.newInstance();
        assert obj instanceof BaseActorSimulator : "Received message for actor type " + actorTypeName + " which has a handler/simulator that does not extend BaseActorSimulator"
        return (BaseActorSimulator) obj;
    }

    /**
     * Returns an instance of the http server for a given simulator
    * @param simId simulator to be loaded.
    * @return instance of the http server for this simulator, or null if this
    * simulator does not have an http server class
    */
   public static BaseActorSimulator getHttpSimulatorRuntime(SimId simId) {
       SimulatorConfig config = GenericSimulatorFactory.getSimConfig(simId);
       String actorTypeName = config.getActorType();
       gov.nist.asbestos.simapi.tk.actors.ActorType actorType = gov.nist.asbestos.simapi.tk.actors.ActorType.findActor(actorTypeName);
       String actorSimClassName = actorType.getHttpSimulatorClassName();
       if (StringUtils.isBlank(actorSimClassName)) return null;
       logger.info("Loading runtime for proxy " + simId + " of type " + actorTypeName + " of class " + actorSimClassName);
       Class<?> clas = Class.forName(actorSimClassName);

       // find correct constructor - no parameters
       Constructor<?>[] constructors = clas.getConstructors();
       Constructor<?> constructor = null;
       for (int i=0; i<constructors.length; i++) {
           Constructor<?> cons = constructors[i];
           Class<?>[] parmTypes = cons.getParameterTypes();
           if (parmTypes.length != 0) continue;
           constructor = cons;
       }
       assert constructor : "Cannot find no-argument constructor for " + actorSimClassName
       Object obj = constructor.newInstance();
       assert obj instanceof BaseActorSimulator : "Received message for actor type " + actorTypeName + " which has a handler/simulator that does not extend BaseActorSimulator"
       return (BaseActorSimulator) obj;
   }

}
