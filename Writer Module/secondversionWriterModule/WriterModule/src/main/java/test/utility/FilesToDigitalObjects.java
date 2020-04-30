package test.utility;

import com.opentext.ia.sdk.sip.DigitalObject;
import com.opentext.ia.sdk.sip.DigitalObjectsExtraction;
import test.beans.SipRecordData;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

public class FilesToDigitalObjects implements DigitalObjectsExtraction<SipRecordData> {

    @Override
    public Iterator<? extends DigitalObject> apply(SipRecordData recordData) {
        return Arrays.stream(getData(recordData)).iterator();
    }

    private DigitalObject[] getData(SipRecordData recordData) {
        DigitalObject[] digObj = new DigitalObject[recordData.getAttachments().size()];
        int i = 0;
        for (String fileitem : recordData.getAttachments()) {
            fileitem = fileitem.replace("/", File.separator);
            File file = new File(fileitem);
            digObj[i++] = (DigitalObject.fromFile(file.getName(), file));
        }
        return digObj;
    }
}
