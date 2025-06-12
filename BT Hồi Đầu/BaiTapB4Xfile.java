import java.io.*;

public class BaiTapB4Xfile {
    private static BaiTapB4Xfile XFile;

    //Ham ghi du lieu nhi phan vao file
    public static void write(String filePath, byte[] data) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(data);
        }
    }

    //ham doc du lieu nhi phan tu file
    public static byte[] read(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] data = new byte[fis.available()];
            fis.read(data);
            return data;
        }
    }
    // Ham ghi mot doi tuong Serializable vao file

    public static void writeObject(String filePath, Serializable object) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(object);
        }
    }
    // Ham doc mot doi tuong Serializable tu file

    public static Object readObject(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return ois.readObject();
        }
    }

    public static void main(String[] args) {
        try {
            String source = "test.txt";
            String destination = "destinationFile.txt";



            // Doc du lieu tu file nguon
            byte[] data = BaiTapB4Xfile.read(source);

            // Ghi dữ liệu vào file đích
            BaiTapB4Xfile.write(destination, data);
            System.out.println("File da duoc sao chep vao file destinationFile.txt!");

            // Chuyen mang byte thanh chuoi
            String fileContent = new String(data);


            // Phan tach noi dung file thanh cac dong
            String[] lines = fileContent.split("\n");


            // Doc tung truong du lieu
            for (String line : lines) {
                // Loai bo khoang trang dau/cuoi dong
                line = line.trim();

                // Hien thi tung dong (truong du lieu)
                System.out.println(line);
            }


            // Su dung writeObject va readObject
            String objectFilePath = "testObject.bin";
            String testString = "Hello, hahaha.";

            // Ghi object vao file
            BaiTapB4Xfile.writeObject(objectFilePath, testString);
            System.out.println("Object da duoc ghi vao file!");

            // Doc object tu file
            String readString = (String) BaiTapB4Xfile.readObject(objectFilePath);
            System.out.println("Object da duoc doc tu file: " + readString);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

/*
Java Swing là một thư viện giao diện người dùng
Java Swing cung cấp rất nhiều thành phần giao diện như:
- Cửa sổ: `JFrame`
- Hộp thoại: `JDialog`
- Nút bấm: `JButton`
- Nhãn: `JLabel`
- Text field: `JTextField`
- Bảng biểu: `JTable`
- Thanh menu: `JMenuBar`
- Và nhiều thành phần tương tác khác.
*/