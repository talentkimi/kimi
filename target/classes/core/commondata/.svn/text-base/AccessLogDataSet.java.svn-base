////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//      FILE NAME       :       AccessLogDataSet.java
//      DATE            :       25 Feb 2009
//      AUTHOR          :       Rob O'Brien
//      REFERENCE       :       OPS-64
//
//      DESCRIPTION     :       Collection of AccessLogData objects.
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

import java.util.Vector;

import core.xml.XmlNode;

public class AccessLogDataSet
{
    /** A <code>Vector</code> containing <code>AccessLogData</code> objects. */
    Vector<AccessLogData> accessLogDataV = new Vector();

    /**
     * Appends a new <code>AccessLogData</code> object to the <code>AccessLogDataSet</code>.
     * @param accessLogData The new <code>AccessLogData</code> object.
     */
    public void add(AccessLogData accessLogData)
    {
        accessLogDataV.add(accessLogData);
    }
    
    /**
     * Formats the <code>AccessLogDataSet</code> object as an <code>XmlNode</code>.
     * @return An <code>XmlNode</code> representation of the <code>AccessLogDataSet</code>.
     */
    public XmlNode toXml()
    {
        XmlNode accessLogListXml = new XmlNode("AccessLogList");
        
        for (AccessLogData data : accessLogDataV)
        {
            accessLogListXml.addChild(data.toXml());
        }
        
        return accessLogListXml;
    }
    
    /**
     * Formats the <code>AccessLogDataSet</code> object as a <code>String</code>.
     * @return A <code>String</code> representation of the <code>AccessLogDataSet</code>.
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        String delimiter = null;
 
        sb.append("AccessLogDataSet=[");
        
        for (AccessLogData data : accessLogDataV)
        {
            sb.append(delimiter != null ? delimiter : "");
            sb.append(data.toString());
            delimiter = delimiter == null ? "," : delimiter;
        }
        
        sb.append("]");
        
        return sb.toString();
    }
}
