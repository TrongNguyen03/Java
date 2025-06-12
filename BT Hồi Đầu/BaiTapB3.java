import java.util.*;
import java.text.DecimalFormat;

public class BaiTapB3 {
    //Bai 1
    static class SVDH2 {
        // lop truu tuong SinhVienDaiHoc
        static abstract class SinhVienDaiHoc {
            protected String hoten;
            protected String nganh;

            public SinhVienDaiHoc(String hoten, String nganh) {
                this.hoten = hoten;
                this.nganh = nganh;
            }

            // Phuong thuc truu tuong lay diem
            public abstract double getdiem();

            // Xep loai hoc luc
            public String gethocluc() {
                double diem = getdiem();
                if (diem >= 9) {
                    return "Xuat sac";
                } else if (diem >= 8.5) {
                    return "Gioi";
                } else if (diem >= 6.5) {
                    return "Kha";
                } else if (diem >= 5) {
                    return "Trung binh";
                } else {
                    return "Yeu";
                }
            }

            public void xuat() {
                System.out.println("Ho va ten: " + hoten);
                System.out.println("Nganh: " + nganh);
                System.out.println("Diem: " + getdiem());
                System.out.println("Hoc luc: " + gethocluc());
            }
        }

        // Lop SinhVienIT ke thua
        static class SinhVienIT extends SinhVienDaiHoc {
            private double diemjava, diemhtml, diemcss;

            public SinhVienIT(String hoten, double diemjava, double diemhtml, double diemcss) {
                super(hoten, "CNTT");
                this.diemjava = diemjava;
                this.diemhtml = diemhtml;
                this.diemcss = diemcss;
            }

            @Override
            public double getdiem() {
                return (diemjava * 2 + diemhtml + diemcss) / 4;
            }
        }

        // Lop SinhVienBiz ke thua
        static class SinhVienBiz extends SinhVienDaiHoc {
            private double diemmarketing, diemhsales;

            public SinhVienBiz(String hoten, double diemmarketing, double diemhsales) {
                super(hoten, "Marketing");
                this.diemmarketing = diemmarketing;
                this.diemhsales = diemhsales;
            }

            @Override
            public double getdiem() {
                return (diemmarketing * 2 + diemhsales) / 3;
            }
        }

        // Phuong thuc nhap danh sach
        public static void nhapDanhSach(Scanner scanner, List<SinhVienDaiHoc> danhsach) {
            System.out.println("Nhap so luong sinh vien:");
            int soluong = scanner.nextInt();
            scanner.nextLine(); // don bo dem

            for (int i = 0; i < soluong; i++) {
                System.out.println("Nhap thong tin sinh vien thu " + (i + 1) + ":");
                System.out.print("Loai sinh vien (1-IT, 2-Biz): ");
                int loai = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Ho va ten: ");
                String hoten = scanner.nextLine();

                if (loai == 1) { // SinhVienIT
                    System.out.print("Diem Java: ");
                    double diemjava = scanner.nextDouble();
                    System.out.print("Diem HTML: ");
                    double diemhtml = scanner.nextDouble();
                    System.out.print("Diem CSS: ");
                    double diemcss = scanner.nextDouble();
                    scanner.nextLine();

                    danhsach.add(new SinhVienIT(hoten, diemjava, diemhtml, diemcss));
                } else if (loai == 2) { // SinhVienBiz
                    System.out.print("Diem Marketing: ");
                    double diemmarketing = scanner.nextDouble();
                    System.out.print("Diem Sales: ");
                    double diemhsales = scanner.nextDouble();
                    scanner.nextLine(); // Don dep bo dem

                    danhsach.add(new SinhVienBiz(hoten, diemmarketing, diemhsales));
                } else {
                    System.out.println("Loai sinh vien khong hop le! Vui long nhap lai.");
                    i--; // Lap lai lan nhap hien tai
                }
            }

            // Sap xep danh sach theo diem sau khi nhap
            sapXepTheoDiem(danhsach);
        }

        // Sap xep theo diem giam dan
        public static void sapXepTheoDiem(List<SinhVienDaiHoc> danhsach) {
            danhsach.sort(Comparator.comparingDouble(SinhVienDaiHoc::getdiem).reversed());
        }

        // Xuat toan bo danh sach sv
        public static void xuatDanhSachTong(List<SinhVienDaiHoc> danhsach) {
            System.out.println("\n=====Danh sach sinh vien=====");
            for (SinhVienDaiHoc sv : danhsach) {
                sv.xuat();
                System.out.println("---");
            }
        }

        // Xuat danh sach sinh vien gioi
        public static void xuatDanhSachSinhVienGioi(List<SinhVienDaiHoc> danhsach) {
            System.out.println("\n======Danh sach sinh vien gioi======");
            boolean coSinhVienGioi = false;
            for (SinhVienDaiHoc sv : danhsach) {
                if (sv.gethocluc().equals("Gioi") || sv.gethocluc().equals("Xuat sac")) {
                    sv.xuat();
                    System.out.println("---");
                    coSinhVienGioi = true;
                }
            }
            if (!coSinhVienGioi) {
                System.out.println("Khong co hoc sinh gioi va xuat sac!");
            }

        }

        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            List<SinhVienDaiHoc> danhsach = new ArrayList<>();

            // Nhap danh sach sinh vien
            nhapDanhSach(scanner, danhsach);

            // Xuat toan bo danh sach
            xuatDanhSachTong(danhsach);

            // Xuat danh sach sinh vien gioi va xuat sac
            xuatDanhSachSinhVienGioi(danhsach);

            scanner.close();
        }
    }

    //Bai 2
    static class CtyRongViet {
        // Lop truu tuong nhan vien cong ty
        static abstract class NhanVienCty {
            protected String hoten;
            protected String maNhanVien;

            public NhanVienCty(String maNhanVien, String hoten) {
                this.maNhanVien = maNhanVien;
                this.hoten = hoten;
            }

            // Phuong thuc truu tuong lay luong
            public abstract double getThuNhap();

            public void xuat() {
                // Xuat thong tin nhan vien
                DecimalFormat df = new DecimalFormat("#,###");
                System.out.println("Ma nhan vien: " + maNhanVien);
                System.out.println("Ho va ten: " + hoten);
                System.out.println("Thu nhap: " + df.format(getThuNhap()));
            }
        }

        // Lop nhan vien hanh chinh ke thua
        static class NhanVienHanhChinh extends NhanVienCty {
            private double luongCoBan; // Luong co ban
            private int ngayCong; // So ngay cong

            public NhanVienHanhChinh(String maNhanVien, String hoten, double luongCoBan, int ngayCong) {
                super(maNhanVien, hoten);
                this.luongCoBan = luongCoBan;
                this.ngayCong = ngayCong;
            }

            @Override
            public double getThuNhap() {
                // Tinh thu nhap nhan vien hanh chinh
                return luongCoBan * ngayCong;
            }
        }

        // Lop nhan vien kinh doanh ke thua
        static class NhanVienKinhDoanh extends NhanVienCty {
            private double luongCoBan; // Luong co ban
            private double luongKinhDoanh; // Luong kinh doanh

            public NhanVienKinhDoanh(String maNhanVien, String hoten, double luongCoBan, double luongKinhDoanh) {
                super(maNhanVien, hoten);
                this.luongCoBan = luongCoBan;
                this.luongKinhDoanh = luongKinhDoanh;
            }

            @Override
            public double getThuNhap() {
                // Tinh thu nhap nhan vien kinh doanh
                return luongCoBan + luongKinhDoanh;
            }
        }

        // Phuong thuc nhap danh sach nhan vien
        public static void nhapDanhSach(Scanner scanner, List<NhanVienCty> danhSach, double luongCoBan) {
            System.out.println("Nhap so luong nhan vien:");
            int soluong = scanner.nextInt();
            scanner.nextLine();

            for (int i = 0; i < soluong; i++) {
                System.out.println("Nhap thong tin nhan vien thu " + (i + 1) + ":");
                String maNhanVien;
                String hoten;

                while (true) {
                    System.out.print("Ma nhan vien (Bat dau bang 1 la Hanh Chinh, 2 la Kinh Doanh): ");
                    maNhanVien = scanner.nextLine();

                    if (maNhanVien.startsWith("1") && maNhanVien.length() == 3) {
                        // Nhap nhan vien hanh chinh
                        System.out.print("Ho va ten: ");
                        hoten = scanner.nextLine();
                        System.out.print("Ngay cong: ");
                        int ngayCong = scanner.nextInt();
                        scanner.nextLine();
                        danhSach.add(new NhanVienHanhChinh(maNhanVien, hoten, luongCoBan, ngayCong));
                        break;
                    } else if (maNhanVien.startsWith("2") && maNhanVien.length() == 3) {
                        // Nhap nhan vien kinh doanh
                        System.out.print("Ho va ten: ");
                        hoten = scanner.nextLine();
                        System.out.print("Luong kinh doanh: ");
                        double luongKinhDoanh = scanner.nextDouble();
                        scanner.nextLine();
                        danhSach.add(new NhanVienKinhDoanh(maNhanVien, hoten, luongCoBan, luongKinhDoanh));
                        break;
                    } else {
                        System.out.println("Ma nhan vien khong hop le (phai du 3 so)! Vui long nhap lai.");
                    }
                }
            }
        }

        // Phuong thuc xuat danh sach nhan vien
        public static void xuatDanhSach(List<NhanVienCty> danhSach) {
            System.out.println("\n=====Danh sach nhan vien=====");
            for (NhanVienCty nv : danhSach) {
                nv.xuat();
                System.out.println("---");
            }
        }

        // Phuong thuc tim kiem nhan vien theo ma
        public static void timKiemNhanVien(List<NhanVienCty> danhSach, String maTimKiem) {
            for (NhanVienCty nv : danhSach) {
                if (nv.maNhanVien.equals(maTimKiem)) {
                    System.out.println("Tim thay nhan vien:");
                    nv.xuat();
                    return;
                }
            }
            System.out.println("Khong tim thay nhan vien co ma " + maTimKiem);
        }

        // Phuong thuc tim kiem nhan vien theo luong
        public static void timKiemNhanVienTheoLuong(List<NhanVienCty> danhSach, Double luongTimKiem) {
            boolean found = false;
            for (NhanVienCty nv : danhSach) {
                if (nv.getThuNhap() == luongTimKiem) {
                    System.out.println("Tim thay nhan vien:");
                    nv.xuat();
                    System.out.println("---");
                    found = true;
                }
            }
            if (!found) {
                System.out.println("Khong tim thay nhan vien co luong = " + luongTimKiem);
            }
        }

        // Phuong thuc xoa nhan vien theo ma
        public static void xoaNhanVien(List<NhanVienCty> danhSach, String maNhanVien) {
            for (NhanVienCty nv : danhSach) {
                if (nv.maNhanVien.equals(maNhanVien)) {
                    System.out.println("Xoa nhan vien co ma " + maNhanVien);
                    danhSach.remove(nv);
                    return;
                }
            }
            System.out.println("Khong tim thay nhan vien can xoa " + maNhanVien);

        }

        // Phuong thuc cap nhat thong tin nhan vien
        public static void capNhatNhanVien(List<NhanVienCty> danhSach, Scanner scanner, String maNhanVien, double luongCoBan) {
            for (NhanVienCty nv : danhSach) {
                if (nv.maNhanVien.equals(maNhanVien)) {
                    System.out.println("Cap nhat thong tin nhan vien:");
                    System.out.print("Ho va ten: ");
                    nv.hoten = scanner.nextLine();

                    if (nv instanceof NhanVienHanhChinh) {
                        // Cap nhat so ngay cong
                        System.out.print("So ngay cong: ");
                        int ngayCong = scanner.nextInt();
                        ((NhanVienHanhChinh) nv).ngayCong = ngayCong;
                    } else if (nv instanceof NhanVienKinhDoanh) {
                        // Cap nhat luong kinh doanh
                        System.out.print("Luong kinh doanh: ");
                        double luongKinhDoanh = scanner.nextDouble();
                        ((NhanVienKinhDoanh) nv).luongKinhDoanh = luongKinhDoanh;
                    }
                    scanner.nextLine();
                    return;
                }
            }
            System.out.println("Khong tim thay nhan vien co ma " + maNhanVien);
        }

        // Phuong thuc sap xep nhan vien theo ten
        public static void sapXepNhanVien(List<NhanVienCty> danhSach) {
            danhSach.sort(Comparator.comparing((NhanVienCty nv) -> nv.hoten));
        }

        // Phuong thuc sap xep nhan vien theo thu nhap
        public static void sapXepNhanVienTN(List<NhanVienCty> danhSach) {
            danhSach.sort(Comparator.comparingDouble(NhanVienCty::getThuNhap).reversed());
        }

        // Phuong thuc xuat top 5 nhan vien co thu nhap cao nhat
        public static void xuatTop5ThuNhap(List<NhanVienCty> danhSach) {
            System.out.println("\n=====Top 5 nhan vien thu nhap cao nhat=====");
            danhSach.stream()
                    .sorted(Comparator.comparingDouble(NhanVienCty::getThuNhap).reversed())
                    .limit(5)
                    .forEach(nv -> {
                        nv.xuat();
                        System.out.println("---");
                    });
        }

        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            List<NhanVienCty> danhSach = new ArrayList<>();
            double luongCoBan = 800000; // Luong co ban

            //nhap danh sach
            nhapDanhSach(scanner, danhSach, luongCoBan);

            //them menu chuc nang
            while (true) {
                System.out.println("\n===== MENU CHUC NANG =====");
                System.out.println("1. Xuat danh sach nhan vien");
                System.out.println("2. Tim kiem nhan vien theo ma");
                System.out.println("3. Tim kiem nhan vien theo luong");
                System.out.println("4. Xoa nhan vien theo ma");
                System.out.println("5. Cap nhat thong tin nhan vien");
                System.out.println("6. Sap xep danh sach nhan vien theo ten");
                System.out.println("7. Sap xep danh sach nhan vien theo thu nhap");
                System.out.println("8. Xuat top 5 nhan vien co thu nhap cao nhat");
                System.out.println("0. Thoat");
                System.out.print("Chon chuc nang: ");
                int chucNang = scanner.nextInt();
                scanner.nextLine(); //don dem

                switch (chucNang) {
                    case 1:
                        xuatDanhSach(danhSach);
                        break;
                    case 2:
                        System.out.print("Nhap ma nhan vien can tim: ");
                        String maTimKiem = scanner.nextLine();
                        timKiemNhanVien(danhSach, maTimKiem);
                        break;
                    case 3:
                        System.out.print("Nhap luong nhan vien can tim: ");
                        Double luongTimKiem = scanner.nextDouble();
                        timKiemNhanVienTheoLuong(danhSach, luongTimKiem);
                        break;
                    case 4:
                        System.out.print("Nhap ma nhan vien can xoa: ");
                        String maXoa = scanner.nextLine();
                        xoaNhanVien(danhSach, maXoa);
                        System.out.println("+=====Danh sach sau khi xoa=====+");
                        xuatDanhSach(danhSach);
                        break;
                    case 5:
                        System.out.print("Nhap ma nhan vien can cap nhat: ");
                        String maCapNhat = scanner.nextLine();
                        capNhatNhanVien(danhSach, scanner, maCapNhat, luongCoBan);
                        System.out.println("+=====Danh sach sau khi cap nhat=====+");
                        xuatDanhSach(danhSach);
                        break;
                    case 6:
                        sapXepNhanVien(danhSach);
                        xuatDanhSach(danhSach);
                        break;
                    case 7:
                        sapXepNhanVienTN(danhSach);
                        xuatDanhSach(danhSach);
                        break;
                    case 8:
                        xuatTop5ThuNhap(danhSach);
                        break;
                    case 0:
                        System.out.println("Thoat chuong trinh.");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Chuc nang khong hop le! Vui long chon lai.");
                }
            }
        }
    }
}