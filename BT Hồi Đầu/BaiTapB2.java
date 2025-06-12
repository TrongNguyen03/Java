import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Pattern;

public class BaiTapB2 {
    //Bai 1
    static class hoten {
        public static void main(String[] arg) {
            Scanner scanner = new Scanner(System.in);

            //Nhap ho va ten
            System.out.println("Nhap ho va ten: ");
            String fullname = scanner.nextLine().trim();

            //tach cac phan cua ho va ten
            String[] parts = fullname.split("\\s+");
            if (parts.length < 2) {
                System.out.println("Vui long nhap day du ho ten!");
            } else {
                //in hoa ho ten
                String ho = parts[0].toUpperCase();
                String ten = parts[parts.length - 1].toUpperCase();

                StringBuilder tendem = new StringBuilder();
                for (int i = 1; i < parts.length - 1; i++) {
                    tendem.append(parts[i]).append(" ");
                }

                //Xuat ket qua
                System.out.println("Ho: " + ho);
                System.out.println("Ten dem: " + tendem);
                System.out.println("Ten: " + ten);
            }
            scanner.close();
        }
    }

    //Bai 2
    static class sanpham {
        private String ten;
        private int gia;
        private String hang;

        public sanpham(String ten, int gia, String hang) {
            this.ten = ten;
            this.gia = gia;
            this.hang = hang;
        }


        //Xuất thông tin
        public void xuatthongtin() {
            DecimalFormat df = new DecimalFormat("#,###");
            System.out.println("Ten san pham: " + ten);
            System.out.println("Gia san pham: " + df.format(gia));
            System.out.println("Hang: " + hang);

        }

        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            sanpham[] products = new sanpham[5];//mang luu san pham

            for (int i = 0; i < 5; i++) {
                System.out.println("Nhap thong tin san pham thu " + (i + 1) + ":");
                System.out.println("Ten san pham: ");
                String ten = scanner.nextLine();

                System.out.println("Nhap gia san pham: ");
                int gia = scanner.nextInt();
                scanner.nextLine();

                System.out.println("Hang san xuat: ");
                String hang = scanner.nextLine();


                products[i] = new sanpham(ten, gia, hang);
            }
            System.out.println("=====Danh sach san pham cua hang Nokia=====");
            boolean found = false;
            for (sanpham product : products) {
                if (product.hang.equalsIgnoreCase("Nokia")) {
                    product.xuatthongtin();
                    found = true;
                }
            }
            if (!found) {
                System.out.println("khong co san pham thuoc hang Nokia");
            }
            scanner.close();
        }
    }

    //Bai 3
    static class sinhvien {
        String Hoten;
        String email;
        String sdt;
        String cmnd;

        public sinhvien(String Hoten, String email, String sdt, String cmnd) {
            this.Hoten = Hoten;
            this.email = email;
            this.sdt = sdt;
            this.cmnd = cmnd;
        }

        //in thong tin
        public void Xuat() {
            System.out.println("Ho va ten: " + Hoten);
            System.out.println("Email: " + email);
            System.out.println("SĐT: " + sdt);
            System.out.println("CMND: " + cmnd);
        }

        //bieu thuc kiem tra dinh dang
        private static final String email_regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        private static final String cmnd_regex = "^(\\d{9}|\\d{12})$"; // CMND co 9 hoac 12 so
        private static final String sdt_regex = "^(0[3-9]\\d{8})$"; // Số điện thoại hợp lệ

        //ham kiem tra hop le
        private static String nhaphople(Scanner scanner, String loinhan, String regex, String tinnhanloi) {
            String input;
            while (true) {
                System.out.print(loinhan);
                input = scanner.nextLine().trim();
                if (Pattern.matches(regex, input)) {
                    return input;//hop le
                }
                System.out.println(tinnhanloi);
            }
        }

        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            sinhvien[] dssinhvien = new sinhvien[5];//mang luu sv

            for (int i = 0; i < 5; i++) {
                System.out.println("Nhap thong tin sinh vien thu " + (i + 1) + ":");
                System.out.println("Ho ten SV: ");
                String Hoten = scanner.nextLine().trim();

                String email = nhaphople(scanner, "Nhap email: ", email_regex, "Sai dinh dang (nhap lai)!");
                String sdt = nhaphople(scanner, "Nhap SDT: ", sdt_regex, "Sai dinh dang (nhap lai)");
                String cmnd = nhaphople(scanner, "Nhap CMND: ", cmnd_regex, "Sai dinh dang (nhap lai)");

                dssinhvien[i] = new sinhvien(Hoten, email, sdt, cmnd);
            }
            //xuat ra danh sach
            for (sinhvien sv : dssinhvien) {
                System.out.println("=====Danh sach sinh vien=====");
                sv.Xuat();
            }
            scanner.close();
        }

    }

    //Bai 4
    static class chuvi_dientich {
        static class chunhat {
            protected double rong;
            protected double dai;

            public chunhat(double rong, double dai) {
                this.rong = rong;
                this.dai = dai;
            }

            //tinh chu vi
            public double getchuvi() {
                return (rong + dai) * 2;
            }

            //tinh dien tich
            public double getdientich() {
                return rong * dai;
            }

            //xuat thong tin
            public void xuat() {
                System.out.println("====Hinh Chu Nhat====");
                System.out.println("Chieu rong: " + rong);
                System.out.println("Chieu dai: " + dai);
                System.out.println("Chu vi : " + getchuvi());
                System.out.println("Dien tich: " + getdientich());
            }
        }

        static class vuong extends chunhat {
            public vuong(double canh) {
                super(canh, canh);
            }
            //ghi de phuong thuc xuat

            @Override
            public void xuat() {
                System.out.println("====Hinh Vuong====");
                System.out.println("Canh: " + rong);
                System.out.println("Chu vi : " + getchuvi());
                System.out.println("Dien tich: " + getdientich());
            }
        }

        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);

            //nhap 2 hinh chu nhat
            chunhat[] danhsachHCN = new chunhat[2];
            for (int i = 0; i < 2; i++) {
                System.out.println("Nhap thong tin HCN thu " + (i + 1) + ":");
                System.out.println("Nhap chieu rong: ");
                double rong = scanner.nextDouble();
                System.out.println("Nhap chieu dai: ");
                double dai = scanner.nextDouble();
                danhsachHCN[i] = new chunhat(rong, dai);
            }

            //nhap 1 hinh vuong
            System.out.println("NHAP HINH VUONG ");
            System.out.println("Nhap canh: ");
            double canh = scanner.nextDouble();
            vuong hinhvuong = new vuong(canh);

            //xuat thong tin cac hinh
            for (chunhat hcn : danhsachHCN) {
                hcn.xuat();
            }
            hinhvuong.xuat();
            scanner.close();
        }
    }

    //Bai 5
    static class SVDH {
        static abstract class SinhVienDaiHoc {
            protected String hoten;
            protected String nganh;

            public SinhVienDaiHoc(String hoten, String nganh) {
                this.hoten = hoten;
                this.nganh = nganh;
            }

            //phuong thuc truu tuong getdiem()
            public abstract double getdiem();

            //xep loai hoc luc
            public String gethocluc() {
                double diem = getdiem();
                if (diem >= 9) return "Xuat sac";
                else if (diem >= 8.5) return "Gioi";
                else if (diem >= 6.5) return "Kha";
                else if (diem >= 5) return "Trung binh";
                else return "Yeu";
            }

            public void xuat() {
                System.out.println("Ho va ten: " + hoten);
                System.out.println("Nganh: " + nganh);
                System.out.println(("Diem: " + getdiem()));
                System.out.println("Hoc luc: " + gethocluc());
            }
        }

        //Bai 6
        static class SinhVienIT extends SinhVienDaiHoc {
            private double diemjava, diemhtml, diemcss;

            public SinhVienIT(String hoten, double diemjava, double diemhtml, double diemcss) {
                super(hoten, "Cong Nghe Thong Tin");
                this.diemjava = diemjava;
                this.diemhtml = diemhtml;
                this.diemcss = diemcss;
            }

            @Override
            public double getdiem() {
                return (diemjava * 2 + diemhtml + diemcss) / 4;
            }
        }

        static class SinhVienBiz extends SinhVienDaiHoc {
            private double diemmarketing, diemhsales;

            public SinhVienBiz(String hoten, double diemmarketing, double diemhsales) {
                super(hoten, "Marketting");
                this.diemmarketing = diemmarketing;
                this.diemhsales = diemhsales;

            }

            @Override
            public double getdiem() {
                return (diemmarketing * 2 + diemhsales) / 3;
            }

        }
        public static void main(String[]args){
            Scanner scanner=new Scanner(System.in);

            //nhap thong tin svIT
            System.out.println("Nhap thong tin sinh vien CNTT:");
            System.out.println("Ho ten: ");
            String hotenIT=scanner.nextLine();
            System.out.println("Diem java: ");
            double diemjava=scanner.nextDouble();
            System.out.println("Diem html: ");
            double diemhtml=scanner.nextDouble();
            System.out.println("Nhap diem css: ");
            double diemcss=scanner.nextDouble();
            SinhVienIT Svit=new SinhVienIT(hotenIT, diemjava,diemhtml,diemcss);

            scanner.nextLine();//xuong dong

            //nhap thong tin svbiz
            System.out.println("Nhap thong tin sinh vien Marketting:");
            System.out.println("Ho ten: ");
            String hotenBiz=scanner.nextLine();
            System.out.println("Diem Marketting: ");
            double diemmarketting=scanner.nextDouble();
            System.out.println("Diem Sales: ");
            double diemsales=scanner.nextDouble();
            SinhVienBiz Svbiz= new SinhVienBiz(hotenBiz,diemmarketting,diemsales);

            System.out.println("======Thong Tin Sinh Vien CNTT======");
            Svit.xuat();
            System.out.println("======Thong Tin Sinh Vien Marketting======");
            Svbiz.xuat();
            scanner.close();
        }

    }
}
