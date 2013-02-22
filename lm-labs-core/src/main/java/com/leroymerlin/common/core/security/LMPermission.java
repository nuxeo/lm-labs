package com.leroymerlin.common.core.security;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.NuxeoGroup;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.runtime.api.Framework;

/**
 * @author <a href="mailto:vincent.dutat@ext.leroymerlin.fr">Vincent Dutat</a>
 *
 */
public class LMPermission implements Comparable<LMPermission> {
	
    private static final Log log = LogFactory.getLog(LMPermission.class);
    public final String name;
    public final boolean granted;
    public final String permission;
	private boolean inherited;
	
	/**
	 * @param name
	 * @param displayName
	 * @param permission
	 * @param granted
	 * @param inherited
	 */
	public LMPermission(String name, String displayName, String permission, boolean granted,
			boolean inherited) {
		this(name, permission, granted);
		this.inherited = inherited;
		this.displayName = displayName;
	}

	/**
	 * @param name
	 * @param permission
	 * @param granted
	 */
	public LMPermission(String name, String permission, boolean granted) {
        this.name = name;
        this.permission = permission;
        this.granted = granted;
	}

	private String displayName;

	/**
	 * @param name
	 * @param displayName
	 * @param permission
	 * @param granted
	 */
	public LMPermission(String name, String displayName, String permission, boolean granted) {
		this(name, permission, granted);
		this.displayName = displayName;
	}

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public boolean isGranted() {
        return granted;
    }
    


    /**
     * Formats a display string. If <code>id</code> is a user name, cancatenates
     * user's first name and last name. Id <code>id</code> is a group name,
     * empty string.
     * 
     * @param id
     *            user or group name
     * @return the display name
     * @throws Exception
     *             If a user management operation fails
     */
    private String getDisplayName(final String id) throws Exception {
        String displayName = StringUtils.EMPTY;
        UserManager userManager = Framework.getService(UserManager.class);
        NuxeoGroup group = userManager.getGroup(id);
        if (group != null) {
            // group    
            // displayName = group.getName();
        } else {
            // user
            if (userManager.getPrincipal(id) != null) {
                if (!StringUtils.isEmpty(userManager.getPrincipal(id)
                        .getFirstName())
                        || !StringUtils.isEmpty(userManager.getPrincipal(id)
                                .getLastName())) {
                    displayName = userManager.getPrincipal(id)
                            .getFirstName() + " "
                            + userManager.getPrincipal(id)
                                    .getLastName();
                }
            }
        }
        return displayName;
    }

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
	    if (displayName == null) {
	        try {
//	            log.debug("getting display name for " + name);
	            displayName = getDisplayName(name);
	        } catch (Exception e) {
	            log.warn("Unable to get name and first name for user " + name);
	        }
	    }
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(LMPermission o) {
		if (this.name.equalsIgnoreCase(o.getName())) {
			return this.permission.toLowerCase().compareTo(o.getPermission().toLowerCase());
		}
		return (this.name.toLowerCase().compareTo(o.getName().toLowerCase()));
	}

	/**
	 * @return the inherited
	 */
	public boolean isInherited() {
		return inherited;
	}

	/**
	 * @param inherited the inherited to set
	 */
	public void setInherited(boolean inherited) {
		this.inherited = inherited;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SpacePermission [displayName=" + displayName + ", inherited="
				+ inherited + ", granted=" + granted + ", name=" + name
				+ ", permission=" + permission + "]";
	}


}
