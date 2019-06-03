package pdf.dmtx

import com.google.zxing.BinaryBitmap
import com.google.zxing.LuminanceSource
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.Result
import com.google.zxing.client.j2se.BufferedImageLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.itextpdf.text.Image
import com.itextpdf.text.pdf.BarcodeQRCode
import com.itextpdf.text.pdf.PdfContentByte
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.PdfStamper
import org.apache.commons.io.IOUtils
import org.apache.pdfbox.cos.COSName
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDResources
import org.apache.pdfbox.pdmodel.graphics.PDXObject
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import org.springframework.web.multipart.commons.CommonsMultipartFile

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

class AddDmtxService {

    def serviceMethod() {

    }

    def copyFileToServer(CommonsMultipartFile file) {
        InputStream inputStream = file.getInputStream()
        OutputStream outputStream = new FileOutputStream(file.getOriginalFilename())
        IOUtils.copy(inputStream, outputStream)
        inputStream.close()
        outputStream.close()
    }

    String generateQR(CommonsMultipartFile file) {
        println "PDF selected: ${file.getOriginalFilename()}"
        println "Content type: ${file.getContentType()}"
        println "Class: ${file.getClass()}"

        String editedFilename = file.getOriginalFilename().replace('.pdf', '_Edited.pdf')
        println "Edited file: ${editedFilename}"

        copyFileToServer(file)

        // Open PDF
        PdfReader reader = new PdfReader(file.getOriginalFilename())
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(editedFilename))
        PdfContentByte over = stamper.getOverContent(1)

        // Generate QR code with original file name encoded onto it
        BarcodeQRCode barcodeQRCode = new BarcodeQRCode(file.getOriginalFilename(), 1000, 1000, null)
        Image codeQrImage = barcodeQRCode.getImage()
        codeQrImage.scaleAbsolute(50, 50)
        // (0, 0) is lower left
        // (width 595 pt, height 842 pt)
        codeQrImage.setAbsolutePosition(500, 730)

        // Add QR code to PDF
        over.addImage(codeQrImage)
        stamper.close()
        reader.close()
        editedFilename
    }

    String[] readQR(CommonsMultipartFile file) {
        println "PDF selected: ${file.getOriginalFilename()}"
        println "Content type: ${file.getContentType()}"
        println "Class: ${file.getClass()}"

        copyFileToServer(file)

        //Load PDF document
        File fileToLoad = new File(file.getOriginalFilename())
        PDDocument document = PDDocument.load(fileToLoad);

        // Get first page
        PDPage firstPage = document.getPage(0)

        def imagesFromFirstPage = []
        def decodedStrings = []

        // Extract and save images from the first page
        PDResources resources = firstPage.getResources();
        for (COSName c : resources.getXObjectNames()) {
            PDXObject o = resources.getXObject(c);
            if (o instanceof PDImageXObject) {
                String imgName = "image" + System.nanoTime() + ".png"
                File imgFile = new File(imgName);
                ImageIO.write(((PDImageXObject) o).getImage(), "png", imgFile)
                imagesFromFirstPage << imgName
                try {
                    String decodedText = decodeQR(imgFile);
                    if (decodedText == null) {
                        println("No QR code found in image ${imgName}")
                        decodedStrings << "Not a QR code"

                    } else {
                        println("Decoded text from image ${imgName}: ${decodedText}");
                        decodedStrings << decodedText
                    }

                } catch (IOException e) {
                    System.out.println("Could not decode QR code, IOException :: " + e.getMessage());
                }

            }
        }

        println(decodedStrings.toArray())
        println "Extracted images: ${imagesFromFirstPage}"
        decodedStrings.toArray()
    }

    def decodeQR(File qrCodeImage) {
        BufferedImage bufferedImage = ImageIO.read(qrCodeImage)
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            println("There is no QR code in the image ${qrCodeImage.name}");
            return null;
        }
    }

    def handleImage(CommonsMultipartFile imageFile) {
        println "Image selected: ${imageFile.getOriginalFilename()}"
        println "Content type: ${imageFile.getContentType()}"
        println "Class: ${imageFile.getClass()}"

        copyFileToServer(imageFile)

        File imgFile = new File(imageFile.getOriginalFilename())
        decodeQR(imgFile)
    }
}
