////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//      FILE NAME       :       AccessLogData.java
//      DATE            :       25 Feb 2009
//      AUTHOR          :       Rob O'Brien
//      REFERENCE       :       OPS-64
//
//      DESCRIPTION     :       Container for access logging data.
//
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//      CHANGE HISTORY
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// DATE     | NAME               | DESCRIPTION                                                                 | REFERENCE
//----------+--------------------+-----------------------------------------------------------------------------+--------------------
// dd/mm/yy |                    |                                                                             |                      
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
package core.commondata;

import core.util.UtilDate;
import core.xml.XmlNode;

public class AccessLogData
{
    /** The process being accessed. */
    private String processName;

    /** The timestamp when the process was accessed. */
    private UtilDate timestamp;
    
    /** The IP address of the person who accessed the process. */
    private String address;
    
    /** The username of the person who accessed the process. */
    private String username;
    
    /** A user specified reason for accessing the process. */
    private String comment;
    
    /**
     * Detail for a single access log entry.
     * @param processName The process being accessed.
     * @param timestamp The timestamp when the process was accessed.
     * @param address The IP address of the person who accessed the process.
     * @param username The username of the person who accessed the process.
     * @param comment A user specified reason for accessing the process.
     */
    public AccessLogData(String processName,
                         UtilDate timestamp,
                         String address,
                         String username,
                         String comment)
    {
        this.processName = processName;
        this.timestamp = timestamp;
        this.address = address;
        this.username = username;
        this.comment = comment;
    }
    
    /**
     * Formats the <code>AccessLogData</code> object as an <code>XmlNode</code>.
     * @return An <code>XmlNode</code> representation of the <code>AccessLogData</code>.
     */
    public XmlNode toXml()
    {
        XmlNode accessLogXml = new XmlNode("AccessLog");

        XmlNode processNameXml = new XmlNode("ProcessName");
        processNameXml.setValue(processName != null ? processName : "");
        accessLogXml.addChild(processNameXml);

        XmlNode timestampXml = new XmlNode("Timestamp");
        timestampXml.setValue(timestamp != null ? timestamp.toString(UtilDate.FORMAT_PATTERN_YEAR) : "");
        accessLogXml.addChild(timestampXml);

        XmlNode addressXml = new XmlNode("IpAddress");
        addressXml.setValue(address != null ? address : "");
        accessLogXml.addChild(addressXml);

        XmlNode usernameXml = new XmlNode("Username");
        usernameXml.setValue(username != null ? username : "");
        accessLogXml.addChild(usernameXml);

        XmlNode commantXml = new XmlNode("Comment");
        commantXml.setValue(comment != null ? comment : "");
        accessLogXml.addChild(commantXml);

        return accessLogXml;        
    }

    /**
     * Formats the <code>AccessLogData</code> object as a <code>String</code>.
     * @return A <code>String</code> representation of the <code>AccessLogData</code>.
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        
        sb.append("AccessLogData=[processName=" + processName + ",");
        sb.append("timestamp=" + timestamp + ",");
        sb.append("address=" + address + ",");
        sb.append("username=" + username + ",");
        sb.append("comment=" + username + "]");
        
        return sb.toString();
    }

    public String getProcessName()
    {
        return processName;
    }

    public String getAddress()
    {
        return address;
    }

    public String getUsername()
    {
        return username;
    }

    public String getComment()
    {
        return comment;
    }
}
