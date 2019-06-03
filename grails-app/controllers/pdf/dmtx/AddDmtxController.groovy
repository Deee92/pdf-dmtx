package pdf.dmtx

import org.springframework.web.multipart.commons.CommonsMultipartFile

class AddDmtxController {

    def addDmtxService

    def index() {

    }

    def handlePDF() {
        CommonsMultipartFile pdfFile = request.getFile('pdfFile')
        String editedFileName
        String[] decodedStrings
        if (params.get('methodToCall').equals("Generate QR"))
            editedFileName = addDmtxService.generateQR(pdfFile)
        else
            decodedStrings = addDmtxService.readQR(pdfFile)

        redirect(action: 'goodbye', params: [editedFileName: editedFileName, decodedStrings: decodedStrings])

    }

    def goodbye() {
        [editedFileName: params.editedFileName, decodedStrings: params.decodedStrings, textDecodedFromImage: params.textDecodedFromImage]
    }

    def uploadImage() {

    }

    def handleImage() {
        CommonsMultipartFile imageFile = request.getFile('imageFile')
        String textDecodedFromImage = addDmtxService.handleImage(imageFile)
        if (textDecodedFromImage == null)
            textDecodedFromImage = "(image not a QR code)"
        redirect(action: 'goodbye', params: [textDecodedFromImage: textDecodedFromImage])
    }
}
