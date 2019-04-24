package gov.nist.asbestos.simapi.tk.simCommon;


import com.fasterxml.jackson.annotation.JsonAutoDetect
import groovy.transform.TypeChecked;

/**
 * Definition for an actor simulator.
 * @author bill
 *
 */
@TypeChecked
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
 class SimulatorConfig implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Globally unique id for this simulator
	 */
	SimId id;
	private String actorType;
	private Date expires;
	private boolean expired = false;
	private String environmentName = null;
	private List<SimulatorConfigElement> elements  = new ArrayList<SimulatorConfigElement>();

     boolean isExpired() { return expired; }
	 void isExpired(boolean is) { expired = is; }

	 boolean checkExpiration() {
		Date now = new Date();
		if (now.after(expires))
			expired = true;
		else
			expired = false;
		return expired;
	}

	// not sure what to do with the other attributes, leave alone for now
	 void add(SimulatorConfig asc) {
		for (SimulatorConfigElement ele : asc.elements) {
			if (getFixedByName(ele.name) == null)
				elements.add(ele);
		}
	}

	 String toString() {
		return id.toString();
	}

	 SimulatorConfig(SimId id, String actorType, Date expiration, String environment) {
		this.id = id;
		this.actorType = actorType;
		expires = expiration;
		this.environmentName = environment;

		gov.nist.asbestos.simapi.tk.actors.ActorType at = gov.nist.asbestos.simapi.tk.actors.ActorType.findActor(actorType);
		if (at != null && at.isFhir())
			this.id.forFhir();
	}

	 List<SimulatorConfigElement> elements() {
		return elements;
	}

	 void add(List<SimulatorConfigElement> elementList) {
		elements.addAll(elementList);
	}
     void add(SimulatorConfigElement ele) { elements.add(ele); }

	 Date getExpiration() {
		return expires;
	}

	 List<SimulatorConfigElement> getFixed() {
		List<SimulatorConfigElement> fixed = new ArrayList<SimulatorConfigElement>();
		for (SimulatorConfigElement ele : elements) {
			if (!ele.isEditable())
				fixed.add(ele);
		}
		return fixed;
	}

	 List<SimulatorConfigElement> getElements() { return elements; }

	 List<SimulatorConfigElement> getUser() {
		List<SimulatorConfigElement> user = new ArrayList<SimulatorConfigElement>();
		for (SimulatorConfigElement ele : elements) {
			if (ele.isEditable())
				user.add(ele);
		}
		return user;
	}

    @Deprecated
	 SimulatorConfigElement	getUserByName(String name) {
		if (name == null)
			return null;

		for (SimulatorConfigElement ele : elements) {
			if (name.equals(ele.name))
				return ele;
		}
		return null;
	}

    @Deprecated
	 SimulatorConfigElement	getFixedByName(String name) {
		if (name == null)
			return null;

		for (SimulatorConfigElement ele : elements) {
			if (name.equals(ele.name))
				return ele;
		}
		return null;
	}

     SimulatorConfigElement getConfigEle(String name) {
        if (name == null)
            return null;

        for (SimulatorConfigElement ele : elements) {
            if (name.equals(ele.name))
                return ele;
        }
        return null;
    }

	 List<SimulatorConfigElement> getEndpointConfigs() {
		List<SimulatorConfigElement> configs = new ArrayList<>();

		for (SimulatorConfigElement config : elements) {
			if (config.type == ParamType.ENDPOINT) {
				configs.add(config);
			}
		}

		return configs;
	}

     void deleteFixedByName(String name) {
		SimulatorConfigElement ele = getFixedByName(name);
		if (ele != null)
			elements.remove(ele);
	}

	 void deleteUserByName(String name) {
		SimulatorConfigElement ele = getUserByName(name);
		if (ele != null)
			elements.remove(ele);
	}

     boolean hasConfig(String name) {
        return getFixedByName(name) != null;
    }

    /**
     * Removes configuration parameter with same name (if found) and adds
     * passed parameter.
    * @param replacement parameter to add/replace
    * @return true if existing parameter was replaced. false if no such
    * parameter was found, and passed parameter was added.
    */
    boolean replace(SimulatorConfigElement replacement) {
       boolean replaced = false;
       Iterator <SimulatorConfigElement> itr = elements.iterator();
       while(itr.hasNext()) {
          SimulatorConfigElement existing = itr.next();
          if (existing.name.equals(replacement.name)) {
             itr.remove();
             replaced = true;
             break;
          }
       }
       elements.add(replacement);
       return replaced;
    }


	 SimId getId() {
		return id;
	}
     void setId(SimId simId) { id = simId; }

	 String getActorType() {
		return actorType;
	}
     void setActorType(String type) { actorType = type; }

     String actorTypeFullName() {
        String actorTypeName = getActorType();
        gov.nist.asbestos.simapi.tk.actors.ActorType type = gov.nist.asbestos.simapi.tk.actors.ActorType.findActor(actorTypeName);
        if (type == null) return actorTypeName;
        return type.getName();
    }

	 SimulatorConfigElement get(String name) {
		for (SimulatorConfigElement ele : elements) {
			if (ele.name.equals(name))
				return ele;
		}
		return null;
	}

	 String getDefaultName() {
		return get("Name").asString(); // + "." + getActorType();
	}

	 String getEndpoint(gov.nist.asbestos.simapi.tk.actors.TransactionType transactionType) {
   		List<SimulatorConfigElement> transEles = getEndpointConfigs();
   		for (SimulatorConfigElement ele : transEles) {
   			if (ele.transType == transactionType)
   				return ele.asString();
		}
		return null;
	}

	 TestSession getTestSession() {
		return id.getTestSession();
	}

	 String getEnvironmentName() { return environmentName; }

	@Override
	 boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SimulatorConfig that = (SimulatorConfig) o;

		if (expired != that.expired) return false;
		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (actorType != null ? !actorType.equals(that.actorType) : that.actorType != null) return false;
		if (expires != null ? !expires.equals(that.expires) : that.expires != null) return false;
		return elements != null ? elements.equals(that.elements) : that.elements == null;
	}

	@Override
	 int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (actorType != null ? actorType.hashCode() : 0);
		result = 31 * result + (expires != null ? expires.hashCode() : 0);
		result = 31 * result + (expired ? 1 : 0);
		result = 31 * result + (elements != null ? elements.hashCode() : 0);
		return result;
	}


}
