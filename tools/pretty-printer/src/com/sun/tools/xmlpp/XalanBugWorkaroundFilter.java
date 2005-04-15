/*
 * @(#)$Id: XalanBugWorkaroundFilter.java,v 1.1 2005-04-15 20:08:21 kohsuke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sun.tools.xmlpp;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * Fixes error in the SAX events generated by Xalan. 
 * 
 * @author
 *     Kohsuke Kawaguchi (kohsuke.kawaguchi@sun.com)
 */
public class XalanBugWorkaroundFilter extends XMLFilterImpl {
    
    public XalanBugWorkaroundFilter( ContentHandler next ) {
        this.setContentHandler(next);
    }
    
    public void startElement(String uri, String local, String qname, Attributes arg3) throws SAXException {
        if(uri==null)   uri="";
        super.startElement(uri, local, qname, arg3);
    }

    public void endElement(String uri, String local, String qname) throws SAXException {
        if(uri==null)   uri="";
        super.endElement(uri, local, qname);
    }
}
