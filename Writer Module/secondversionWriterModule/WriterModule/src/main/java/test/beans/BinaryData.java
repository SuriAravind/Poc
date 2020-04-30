package test.beans;

import test.utility.blobbean.BlobInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static test.utility.others.Utility.checkValidFolder;

public class BinaryData {
    
    private static final Logger LOGGER = Logger.getLogger(BinaryData.class.getName());
    
    private final boolean hasData;
    private final String data;
    private final Blob blob;
    
    public BinaryData() {
        data = null;
        hasData = false;
        blob = null;
    }
    
    public BinaryData(final String data, Blob blob) {
        this.blob = blob;
        this.data = data;
        hasData = true;
    }
    
    public Blob getBlob() {
        return blob;
    }
    
    public boolean hasData() {
        return hasData;
    }
    
    @Override
    public String toString() {
        if (hasData) {
            return data;
        } else {
            return "<binary>";
        }
    }
    
    public BlobInfo processBlob(String tableName, String columnName, long recordsProcessed,
                                String outputLocation) {
        outputLocation+=  (outputLocation.endsWith(File.separator) ? "" : File.separator);
        BlobInfo blobInfo = new BlobInfo();
        String validFileName = UUID.randomUUID().toString().substring(0, 14) + new Date().getTime();
        String folder = outputLocation + "BLOBs"
                        + File.separator + checkValidFolder(tableName.toUpperCase()) + File.separator
                        + checkValidFolder(columnName.toUpperCase()) + File.separator + "Folder-"
                        + (((recordsProcessed / 250) * 250) + 1) + "-" + (((recordsProcessed / 250) * 250) + 250)
                        + File.separator;
    
        new File(folder).mkdirs();
        String file = folder + validFileName;
    
        try {
            InputStream in = blob.getBinaryStream();
            OutputStream out = new FileOutputStream(file);
            byte[] buff = new byte[1024];
            int len = 0;
        
            while ((len = in.read(buff)) != -1) {
                out.write(buff, 0, len);
            }
        
            out.flush();
            out.close();
            in.close();
        
            blobInfo.setPath(file.replace(outputLocation,".." + File.separator));
            blobInfo.setName(new File(file).getName());
            blobInfo.setStatus("Success");
            try {
                blobInfo.setSize(Math.ceil(((double) (new File(file).length()) / (double) 1024)) + " KB");
            } catch (Exception e) {
                blobInfo.setSize("NA");
            }
        } catch (Exception e) {
            blobInfo.setPath("");
            blobInfo.setName(new File(file).getName());
            blobInfo.setStatus("Failure");
            blobInfo.setSize("NA");
            LOGGER.log(Level.SEVERE, "Blob Extraction failed ", e);
        }
        return blobInfo;
    }
    
}
