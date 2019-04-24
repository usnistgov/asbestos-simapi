package gov.nist.asbestos.simapi.tk.simCommon;

import com.fasterxml.jackson.annotation.JsonAutoDetect
import groovy.transform.TypeChecked;

@TypeChecked
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
class SimulatorConfigElement implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * Parameter name
	 */
	String name = null;
	/**
	 * Parameter type
	 */
	ParamType type = null;
	gov.nist.asbestos.simapi.tk.actors.TransactionType transType = null;


	// cannot use Object class - will not serialize so tricks are necessary
	enum ValueType { BOOLEAN, STRING , SINGLE_SELECT_LIST, MULTI_SELECT_LIST, SIMPLE_LIST, PATIENT_ERROR_MAP};
	private ValueType valueType = ValueType.STRING;
	private boolean booleanValue = false;
	private String  stringValue = "";
	private List<String> listValue = new ArrayList<>();
	private gov.nist.asbestos.simapi.toolkit.configDatatypes.client.PatientErrorMap patientErrorMap = new gov.nist.asbestos.simapi.toolkit.configDatatypes.client.PatientErrorMap();
	private String extraValue = "";
	private boolean tls = false;

	private boolean editable = false;

	SimulatorConfigElement() {   }

	SimulatorConfigElement(String name, ParamType type, Boolean value) {
		this.name = name;
		this.type = type;
		setBooleanValue(value);
	}

	SimulatorConfigElement(String name, ParamType type, String value) {
		this.name = name;
		this.type = type;
		setStringValue(value);
	}

	SimulatorConfigElement(String name, ParamType type, List<String> values, boolean isMultiSelect) {
		this.name = name;
		this.type = type;
		setListValueWithType(values, ((isMultiSelect) ? ValueType.MULTI_SELECT_LIST : ValueType.SINGLE_SELECT_LIST));
	}

	SimulatorConfigElement(String name, ParamType type, List<String> values) {
		this.name = name;
		this.type = type;
		setListValueWithType(values, ValueType.SIMPLE_LIST);
	}

	SimulatorConfigElement(String name, ParamType type, String[] vals, boolean isMultiSelect) {
		this.name = name;
		this.type = type;
		List<String> values = new ArrayList<>();
		for (String value : vals) values.add(value);
		setListValueWithType(values, ((isMultiSelect) ? ValueType.MULTI_SELECT_LIST : ValueType.SINGLE_SELECT_LIST));
	}

	SimulatorConfigElement(String name, ParamType type, gov.nist.asbestos.simapi.toolkit.configDatatypes.client.PatientErrorMap value) {
		this.name = name;
		this.type = type;
		setPatientErrorMapValue(value);
	}

	String getExtraValue() {
		return extraValue;
	}

	void setExtraValue(String extraValue) {
		this.extraValue = extraValue;
	}

	boolean isEditable() { return editable; }
	void setEditable(boolean v) { editable = v; }

	gov.nist.asbestos.simapi.toolkit.configDatatypes.client.PatientErrorMap asPatientErrorMap() {
		return patientErrorMap;
	}

	String asString() {
		if (valueType == ValueType.STRING)
			return stringValue;
		if (valueType == ValueType.SINGLE_SELECT_LIST && listValue != null && listValue.size() > 0)
			return listValue.get(0);
		return Boolean.toString(false);
	}

	Boolean asBoolean() {
		if (valueType == ValueType.STRING) {
			String v = stringValue;
			v = v.toLowerCase();
			if (v.startsWith("t")) {
				booleanValue = Boolean.TRUE;
				valueType = ValueType.BOOLEAN;
			}
			else if (v.startsWith("f")) {
				booleanValue = Boolean.FALSE;
				valueType = ValueType.BOOLEAN;
			}
		}
		if (valueType == ValueType.BOOLEAN) return booleanValue;

		return false;
	}

	List<String> asList() { return listValue; }

	boolean hasBoolean() { return valueType == ValueType.BOOLEAN;  }
	boolean hasString() { return valueType == ValueType.STRING;  }
	boolean hasSingleList() { return valueType == ValueType.SINGLE_SELECT_LIST; }
	boolean hasMultiList() { return valueType == ValueType.MULTI_SELECT_LIST; }
	boolean hasList() { return hasSingleList() || hasMultiList() || valueType == ValueType.SIMPLE_LIST; }
	// removed because it breaks Jackson serialization
//     boolean isPatientErrorMap() { return valueType == ValueType.PATIENT_ERROR_MAP; }

	String toString() {
		StringBuffer buf = new StringBuffer();

		buf.append("name=").append(name);
		if (tls)
			buf.append(" tls");
		buf.append(" type=").append(type);
		buf.append(" transType=").append(transType);
		if (valueType == ValueType.BOOLEAN)
			buf.append(" boolean value=").append(booleanValue);
		else if (valueType == ValueType.MULTI_SELECT_LIST)
			buf.append(" multiSelectList=").append(listValue);
		else if (valueType == ValueType.SINGLE_SELECT_LIST)
			buf.append(" singleSelectList=").append(listValue);
		else if (valueType == ValueType.PATIENT_ERROR_MAP)
			buf.append(" patientErrorList=").append(patientErrorMap);
		else
			buf.append(" string value=").append(stringValue);

//		buf.append(" values=").append(values);

		buf.append(" editable=").append(isEditable());

		return buf.toString();
	}

	void setListValueWithType(List<String> o, ValueType valueType) { listValue = o; this.valueType = valueType; }

	String getName() {
		return name;
	}

	void setStringValue(String o) {
		if (o != null) o = o.trim();
		stringValue = o;
		valueType = ValueType.STRING;
	}
	String getStringValue() { return stringValue; }

	void setBooleanValue(Boolean o) { booleanValue = o; valueType = ValueType.BOOLEAN; }
	boolean getBooleanValue() { return booleanValue; }

	void setStringListValue(List<String> o) { listValue = o; }
	List<String> getStringListValue() { return listValue; }

	void setPatientErrorMapValue(gov.nist.asbestos.simapi.toolkit.configDatatypes.client.PatientErrorMap o) { patientErrorMap = o; valueType = ValueType.PATIENT_ERROR_MAP; }
	gov.nist.asbestos.simapi.toolkit.configDatatypes.client.PatientErrorMap getPatientErrorMapValue () { return patientErrorMap; }

	List<String> getMultiListValue() { return listValue; }

	@Override
	boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SimulatorConfigElement that = (SimulatorConfigElement) o;

		if (tls != that.tls) return false;
		if (booleanValue != that.booleanValue) return false;
		if (editable != that.editable) return false;
		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		if (type != that.type) return false;
		if (transType != that.transType) return false;
		if (valueType != that.valueType) return false;
		if (!stringValue.equals(that.stringValue)) return false;
		if (!listValue.equals(that.listValue)) return false;
		if (!patientErrorMap.equals(that.patientErrorMap)) return false;
		return extraValue.equals(that.extraValue);

	}

	@Override
	int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (type != null ? type.hashCode() : 0);
		result = 31 * result + (transType != null ? transType.hashCode() : 0);
		result = 31 * result + valueType.hashCode();
		result = 31 * result + (tls ? 1 : 0);
		result = 31 * result + (booleanValue ? 1 : 0);
		result = 31 * result + stringValue.hashCode();
		result = 31 * result + listValue.hashCode();
		result = 31 * result + patientErrorMap.hashCode();
		result = 31 * result + extraValue.hashCode();
		result = 31 * result + (editable ? 1 : 0);
		return result;
	}

	boolean isTls() {
		return tls;
	}

	void setTls(boolean tls) {
		this.tls = tls;
	}
}
