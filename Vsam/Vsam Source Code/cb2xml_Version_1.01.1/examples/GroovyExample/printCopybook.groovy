/**
 * Need to add ../../lib/cb2xml.jar & ../../lib/cb2xml_Jaxb.jar to the classpath
 * The run.bat/sh will do this
 */
import net.sf.cb2xml.Cb2Xml3
import net.sf.cb2xml.def.IItem
    
 
    // Parsing a Cobol copybook
    def cpy = Cb2Xml3.newBuilder("cbl2xml_Test110.cbl")
                     .asCobolItemTree();
    
    // Now lets print it
    print("    ", cpy.getChildItems())

// Print Cobol item (and its child items
def print(String indent, List<IItem> items) {

    for (IItem item : items) {
       String s = indent + item.getFieldName() + "                                                                           "
       s = s.substring(0, 50)
       println s + item.getPosition() + "\t" + item.getStorageLength() + "\t" + item.getDisplayLength() + cn(item.getPicture()) + cn(item.getUsage().getName()) + item.getNumericClass().numeric 
      
       print(indent + "    ", item.getChildItems())
   }
}

// add tab to object and convert to "" if null
def cn(obj) {
    if (obj == null) {
        return "\t"
    }
    return "\t" +obj
}