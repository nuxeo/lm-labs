/**
 * 
 */
package com.leroymerlin.common.core.security;


/**
 * @author <a href="mailto:vincent.dutat@ext.leroymerlin.fr">Vincent Dutat</a>
 *
 */
//@BadgerFish
//@XmlRootElement(name="suggest")
//@XmlAccessorType(XmlAccessType.FIELD)
public class GroupUserSuggest implements Comparable<GroupUserSuggest> {
	/**
	 * @param name
	 * @param fullName
	 */
	public GroupUserSuggest(String name, String fullName) {
		super();
		this.name = name;
		this.fullName = fullName;
	}
	private String name;
	private String fullName;
	/**
	 * @return the name
	 */
//	@XmlElement
	public String getName() {
		return name;
	}
	/**
	 * @return the fullName
	 */
//	@XmlElement
	public String getFullName() {
		return fullName;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public int compareTo(GroupUserSuggest o) {
		return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
	}

    @Override
    public String toString() {
        return GroupUserSuggest.class.getSimpleName() + " [" + this.name + ", " + this.fullName + "]";
    }
}
