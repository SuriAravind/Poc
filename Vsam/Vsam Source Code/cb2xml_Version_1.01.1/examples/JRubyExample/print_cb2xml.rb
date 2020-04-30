################################################################
## This is a simple example of accessing cb2xml from ruby
## It simply prints the content of the Xml
## Because we are reading the cb2xml-Xml (rather than Cobol),
## we only need the cb2xml_Jaxb.jar
##
## Author: Bruce Martin
## License: Any, e.g. LGPL (any), Apache, Creative Commons
################################################################

require '../../lib/cb2xml.jar'

##java_import 'net.sf.cb2xml.parse.XmlParser'
java_import 'net.sf.cb2xml.Cb2Xml3'
java_import 'net.sf.cb2xml.def.IItem'
 
   def printXml indent, items

       items.each do |item|
           puts "#{indent} #{item.getLevelString()} #{item.getFieldName()}\t#{item.getPosition()}\t#{item.getStorageLength()}\t#{item.getPicture()}\t#{item.getUsage().getName()}"   
           printXml "#{indent}   ", item.getChildItems()   
       end
   end

   copybook = Cb2Xml3.newBuilder("cbl2xml_Test110.cbl") .asCobolItemTree()
   
   printXml "    ", copybook.getChildItems()
   
   x = gets
