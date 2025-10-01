package com.example.glogreader.util;

import com.example.glogreader.model.Folder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PDFUtil {

    public static void extractGlog(Model model, MultipartFile glog, MultipartFile book) throws FileNotFoundException, IOException {
        String message = "";

        model.addAttribute("glog", glog.getOriginalFilename());
        model.addAttribute("book", book.getOriginalFilename());

        if (!glog.isEmpty() && !book.isEmpty()) {
            if (glog.getOriginalFilename().substring(glog.getOriginalFilename().lastIndexOf('.')).equals(".pdf")) {
                try {
                    File pdf = new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString() + ".pdf");

                    try (InputStream in = glog.getInputStream();
                         FileOutputStream fos = new FileOutputStream(pdf)) {

                        byte[] buffer = new byte[8192];
                        int len;
                        while ((len = in.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }

                    String string = "";
                    String retornoGLOG = "";
                    String fluxo = "";
                    String centroCusto = "";

                    PDFTextStripper pdfTextStripper = new PDFTextStripper();

                    try (PDDocument document = PDDocument.load(pdf)) {
                        String conteudoGlog = pdfTextStripper.getText(document);

                        String strFluxo = new String(conteudoGlog.replaceAll(" ", ""));

                        if (strFluxo.contains("IFLUXO-EXTER:")) {
                            Integer inicioPosicaoFluxo = strFluxo.lastIndexOf("IFLUXO-EXTER:");

                            fluxo = strFluxo.substring(inicioPosicaoFluxo + 13, inicioPosicaoFluxo + 17);
                        }

                        List<String> list = Arrays.asList(conteudoGlog.split(System.getProperty("line.separator")));

                        Integer contador = 0;

                        for (int i = 0; i < list.size(); i++) {
                            string = list.get(i);

                            if (contador <= 0) {
                                if (string.contains("00001:")) {
                                    centroCusto = string.substring(76, 80);

                                    if (centroCusto.equalsIgnoreCase(fluxo)) {
                                        contador = Integer.parseInt(string.substring(9, 14));

                                        if (string.length() > 75) {
                                            retornoGLOG += PDFUtil.preencheString(string.substring(76), 20, ' ', false);
                                        } else {
                                            retornoGLOG += PDFUtil.preencheString("", 20, ' ', false);
                                        }
                                    }
                                }
                            } else {
                                contador += 20;

                                if (string.contains(contador.toString())) {
                                    contador = Integer.parseInt(string.substring(9, 14));

                                    if (string.length() > 75) {
                                        retornoGLOG += PDFUtil.preencheString(string.substring(76), 20, ' ', false);
                                    } else {
                                        retornoGLOG += PDFUtil.preencheString("", 20, ' ', false);
                                    }
                                } else {
                                    contador -= 20;
                                }
                            }
                        }

                        if (retornoGLOG.length() <= 0) {
                            message = "The imported glog file does not contain log to be listed!";

                            model.addAttribute("message", message);
                        } else {
                            String bookFileName = getFileNameWithoutExtension(book.getOriginalFilename());

                            if (!textFileContainsString(book, bookFileName)) {
                                message = "The imported book file does not contain book structure!";

                                model.addAttribute("message", message);
                            } else {
                                // New condition: compare first 8 bytes of retornoGLOG to stringToCheck
                                String stringToCheck = bookFileName; // or set as needed

                                String retornoGLOGPrefix = retornoGLOG.length() >= 8 ? retornoGLOG.substring(0, 8) : retornoGLOG;

                                if (!retornoGLOGPrefix.equals(stringToCheck)) {
                                    message = "The book imported does not belong to the log imported!";
                                    model.addAttribute("message", message);
                                } else {
                                    message = retornoGLOG;

                                    model.addAttribute("message", message);

                                    model.addAttribute("success", "S");

                                    String contentBook = readCopybookFromMultipartFile(book);

                                    model.addAttribute("folder", buildFromBook(contentBook, retornoGLOG));

                                    ObjectMapper mapper = new ObjectMapper();

                                    model.addAttribute("folderJson", mapper.writeValueAsString(buildFromBook(contentBook, retornoGLOG)));
                                }
                            }
                        }
                    }

                    if (pdf.exists()) {
                        pdf.delete();
                    }
                } catch (Exception e) {
                    message = e.getMessage();

                    model.addAttribute("message", message);
                }
            } else {
                message = "The imported file must be a .pdf!";

                model.addAttribute("message", message);
            }
        } else {
            message = "No file(s) imported!";

            model.addAttribute("message", message);
        }
    }

    public static String readCopybookFromMultipartFile(MultipartFile book) throws IOException {
        StringBuilder copybookContent = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(book.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                copybookContent.append(line).append("\n");
            }
        }

        return copybookContent.toString();
    }

    public static Folder buildFromBook(String bookLayout, String retornoGLOG) {
        List<FieldDef> fields = parseBookLayout(bookLayout);
        int[] pos = {0};

        String bookName = extractBookName(bookLayout);
        Folder root = new Folder("<b>" + bookName + "</b>");

        for (FieldDef field : fields) {
            root.addChild(parseFieldWithData(field, retornoGLOG, pos));
        }

        return root;
    }

    private static String extractBookName(String bookLayout) {
        Pattern commentPattern = Pattern.compile("(?i).*NOME\\s+BOOK\\s*:\\s*(\\S+).*");
        for (String line : bookLayout.split("\\r?\\n")) {
            Matcher matcher = commentPattern.matcher(line);
            if (matcher.matches()) {
                return matcher.group(1);
            }
        }

        Pattern headerPattern = Pattern.compile("\\d{2}\\s+(\\S+)-HEADER\\.");
        for (String line : bookLayout.split("\\r?\\n")) {
            Matcher matcher = headerPattern.matcher(line.trim());
            if (matcher.matches()) {
                return matcher.group(1);
            }
        }

        return "BOOK";
    }

    private static Folder parseFieldWithData(FieldDef field, String data, int[] pos) {
        if (field.occurs > 0) {
            Folder occursParent = new Folder("<b>" + field.label.toUpperCase() + "</b>", 0, field.occurs);
            for (int i = 0; i < field.occurs; i++) {
                Folder occurrence = new Folder("<b>OCORRENCIA #" + (i + 1) + "</b>");
                for (FieldDef child : field.children) {
                    occurrence.addChild(parseFieldWithData(child, data, pos));
                }
                occursParent.addChild(occurrence);
            }
            return occursParent;
        } else if (field.isGroup) {
            Folder group = new Folder("<b>" + field.label + "</b>", field.length, 0);
            for (FieldDef child : field.children) {
                group.addChild(parseFieldWithData(child, data, pos));
            }
            return group;
        } else {
            if (pos[0] + field.length > data.length()) {
                return new Folder("<b>" + field.label + ":</b> [TRUNCATED]", field.length, 0);
            }
            String val = data.substring(pos[0], pos[0] + field.length);
            pos[0] += field.length;

            if (isNumeric(val)) {
                return new Folder("<b>" + field.label + ":</b> " + "<font color=blue>" + replaceTrailingSpacesWithAsterisk(val) + "</font>", field.length, 0);
            }
            return new Folder("<b>" + field.label + ":</b> " + "<font color=green>" + replaceTrailingSpacesWithAsterisk(val) + "</font>", field.length, 0);
        }
    }

    private static boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }

    public static String replaceTrailingSpacesWithAsterisk(String str) {
        if (str == null || str.isEmpty()) return str;

        int end = str.length() - 1;

        int trailingSpaces = 0;
        while (end >= 0 && str.charAt(end) == ' ') {
            trailingSpaces++;
            end--;
        }

        if (trailingSpaces == 0) {
            return str;
        }

        StringBuilder sb = new StringBuilder();

        sb.append(str.substring(0, end + 1));

        for (int i = 0; i < trailingSpaces; i++) {
            sb.append('*');
        }

        return sb.toString();
    }

    static class FieldDef {
        public int level;
        public String label;
        public int length;
        public boolean isGroup;
        private String dependingOn;
        public int occurs = 0;
        public List<FieldDef> children = new ArrayList<>();

        public FieldDef(int level, String label, int length, boolean isGroup) {
            this.level = level;
            this.label = label;
            this.length = length;
            this.isGroup = isGroup;
        }

        public void addChild(FieldDef child) {
            children.add(child);
        }

        public void setDependingOn(String dependingOn) {
            this.dependingOn = dependingOn;
        }

        public String getDependingOn() {
            return dependingOn;
        }

        @Override
        public String toString() {
            return "FieldDef{" + "level=" + level + ", label='" + label + '\'' + ", length=" + length +
                    ", isGroup=" + isGroup + ", occurs=" + occurs + ", children=" + children.size() + '}';
        }
    }

    public static List<FieldDef> parseBookLayout(String book) {
        List<FieldDef> fields = new ArrayList<>();
        Stack<FieldDef> stack = new Stack<>();

        Pattern pattern = Pattern.compile(
                "^(\\d{2})\\s+(\\S+)" +                                    // Level and name
                        "(?:\\s+OCCURS(?:\\s+(\\d+))?(?:\\s+TO\\s+(\\d+))?\\s+TIMES" +  // OCCURS min TO max TIMES
                        "(?:\\s+DEPENDING\\s+ON\\s+(\\S+))?)?" +                  // DEPENDING ON field
                        "(?:\\s+PIC\\s+([X9V\\(\\)0-9]+))?.*"
        );

        for (String line : book.split("\\r?\\n")) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("*")) continue;

            Matcher m = pattern.matcher(line);
            if (!m.find()) continue;

            int level = Integer.parseInt(m.group(1));
            String rawName = m.group(2);
            String occursMin = m.group(3);  // Optional min
            String occursMax = m.group(4);  // Optional max
            String dependingOn = m.group(5); // Optional depending on field
            String pic = m.group(6);        // Optional PIC clause

            boolean isGroup = (pic == null);
            int length = 0;

            if (pic != null) {
                length = parsePicLength(pic);
            }

            int occurs = 0;
            if (occursMax != null) {
                occurs = Integer.parseInt(occursMax);
            } else if (occursMin != null) {
                occurs = Integer.parseInt(occursMin);
            }

            FieldDef f = new FieldDef(level, rawName, length, isGroup);
            f.occurs = occurs;

            if (dependingOn != null) {
                f.setDependingOn(dependingOn);
            }

            while (!stack.isEmpty() && stack.peek().level >= level) {
                stack.pop();
            }

            if (stack.isEmpty()) {
                fields.add(f);
            } else {
                stack.peek().addChild(f);
            }

            if (isGroup || occurs > 0) {
                stack.push(f);
            }
        }

        return fields;
    }

    private static int parsePicLength(String pic) {
        int length = 0;
        pic = pic.replace("V", "");  // remove implied decimal

        Pattern p = Pattern.compile("([X9])(?:\\((\\d+)\\))?");
        Matcher m = p.matcher(pic);

        while (m.find()) {
            String countStr = m.group(2);
            int count = (countStr == null) ? 1 : Integer.parseInt(countStr);
            length += count;
        }
        return length;
    }

    public static File createSamplePDF() throws IOException {
        File file = new File("sample.pdf");

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("         00001:                                                             Pdf Upload");
                contentStream.endText();
            }

            document.save(file);
        }

        return file;
    }

    public static Folder convertFieldDefToFolder(FieldDef f) {
        Folder folder = new Folder(f.label, f.length, f.occurs);
        for (FieldDef child : f.children) {
            folder.addChild(convertFieldDefToFolder(child));
        }
        return folder;
    }

    public static Folder convertListToFolderTree(List<FieldDef> fields) {
        Folder root = new Folder("Book Structure");
        for (FieldDef f : fields) {
            root.addChild(convertFieldDefToFolder(f));
        }
        return root;
    }

    public static boolean textFileContainsString(MultipartFile textFile, String stringToCheck) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(textFile.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.toUpperCase().contains(stringToCheck.toUpperCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getFileNameWithoutExtension(String filename) {
        if (filename == null) return "";
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? filename : filename.substring(0, dotIndex);
    }

    public static String preencheString(String entrada, int tamanho, char preencher, boolean esquerda) {
        if (entrada == null || entrada.equals("null")) {
            entrada = "";
        }

        if (entrada.length() == tamanho) {
            return entrada;
        } else if (entrada.length() > tamanho) {
            return entrada.substring(0, tamanho);
        } else {
            StringBuilder entradaBuilder = new StringBuilder();

            if (!esquerda) {
                entradaBuilder.append(entrada);
            }

            for (int i = 0; i < tamanho; i++) {
                int verificaTamanhoEntrada = entrada.length() + i;

                if (verificaTamanhoEntrada == tamanho) {
                    if (esquerda) {
                        entradaBuilder.append(entrada);
                    }

                    break;
                }

                entradaBuilder.append(preencher);
            }

            return entradaBuilder.toString();
        }
    }
}