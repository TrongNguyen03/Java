import java.text.DecimalFormat;
import java.util.*;
public class BaiTap {
//Bài 1
    static class SanPhamv1{
         private String ten;
         private int gia;
         private int GiamGia;

         public SanPhamv1(String ten, int gia, int GiamGia){
             this.ten=ten;
             this.gia= gia;
             this.GiamGia= GiamGia;
         }

         //phương thức tính thuế
         public double tinhthuenhapkhau(){
             return gia*0.1;
         }

         //Xuất thông tin
         public void xuatthongtin(){
             DecimalFormat df = new DecimalFormat("#,###");
             System.out.println("Ten san pham: "+ ten);
             System.out.println("Gia san pham: "+df.format(gia));
             System.out.println("Giam gia: "+ df.format(GiamGia));
             System.out.println("Thue nhap khau: "+ df.format(tinhthuenhapkhau()));
             System.out.println("Don Gia: "+ df.format((gia+tinhthuenhapkhau()-GiamGia)));


         }
         public static void main(String[] args) {
             SanPhamv1 sp= new SanPhamv1("Tu lanh", 10000000, 200000);
             sp.xuatthongtin();
         }

     }

// // Bài 2
 static class SanPhamv2{
     private String ten;
     private int gia;
     private int GiamGia;

     public SanPhamv2(String ten, int gia, int GiamGia){
         this.ten=ten;
         this.gia= gia;
         this.GiamGia= GiamGia;
     }

     //phương thức tính thuế
     public double tinhthuenhapkhau(){
         return gia*0.1;
     }

     //Xuất thông tin
     public void xuatthongtin(){
         DecimalFormat df = new DecimalFormat("#,###");
         System.out.println("HANG HOA ");
         System.out.println("Ten san pham: "+ ten);
         System.out.println("Gia san pham: "+df.format(gia));
         System.out.println("Giam gia: "+ df.format(GiamGia));
         System.out.println("Thue nhap khau: "+ df.format(tinhthuenhapkhau()));
         System.out.println("Don Gia: "+ df.format((gia+tinhthuenhapkhau()-GiamGia)));


     }
     public static void main(String[] args) {
         Scanner scanner=new Scanner(System.in);
         ArrayList<SanPhamv2> danhsach =new ArrayList<>();
         System.out.println("Nhap so luong san pham:");
         int soluong=scanner.nextInt();
         scanner.nextLine();

         for(int i=0;i< soluong;i++){
             System.out.println("Nhap thong tin san pham thu "+(i+1)+":");
             System.out.println("Ten san pham: ");
             String ten=scanner.nextLine();
             System.out.println("Nhap gia san pham: ");
             int gia = scanner.nextInt();
             System.out.println("Giam gia: ");
             int GiamGia=scanner.nextInt();
             scanner.nextLine();
             danhsach.add(new SanPhamv2(ten, gia, GiamGia));
         }
         System.out.println("=========Danh Sach Hang Hoa=========");
         for (SanPhamv2 sp: danhsach){
             sp.xuatthongtin();
         }
         scanner.close();
     }

 }

// Bài 3
static class SanPham{
    private String ten;
    private int gia;

    public SanPham(String ten, int gia){
        this.ten=ten;
        this.gia= gia;
    }


    //Xuất thông tin
    public void xuatthongtin(){
        DecimalFormat df = new DecimalFormat("#,###");
        System.out.println("HANG HOA ");
        System.out.println("Ten san pham: "+ ten);
        System.out.println("Gia san pham: "+df.format(gia));
    }
    public int getgia(){
        return gia;
    }
    public String getten(){
        return ten;
    }
    //hàm điền sp
    public static void nhapsanpham(Scanner scanner,List<SanPham> danhsach){
        System.out.println("Nhap so luong san pham:");
        int soluong=scanner.nextInt();
        scanner.nextLine();

        for(int i=0;i< soluong;i++){
            System.out.println("Nhap thong tin san pham thu "+(i+1)+":");
            System.out.println("Ten san pham: ");
            String ten=scanner.nextLine();
            System.out.println("Nhap gia san pham: ");
            int gia = scanner.nextInt();
            scanner.nextLine();
            danhsach.add(new SanPham(ten,gia));
        }
    }

    //hàm sắp xếp sản phẩm
    public static void sapxephanghoa(List<SanPham> danhsach){
        danhsach.sort((sp1,sp2) -> Integer.compare(sp2.getgia(), sp1.getgia()));
    }

    //hàm hiển thị mức giá trung bình
    public static double giatb(List<SanPham> danhsach){
      if(danhsach.isEmpty()) return 0;
      int tonggia=danhsach.stream().mapToInt(SanPham::getgia).sum();
      return tonggia / danhsach.size();
    }

    //Hàm hiển thị danh sách lúc đầu
    public static void hienthisanpham(List<SanPham> danhsach){
        DecimalFormat df = new DecimalFormat("#,###");
        System.out.println("=========Danh Sach Hang Hoa=========");
        if (danhsach.isEmpty()) {
         System.out.println("Khong co san pham");
         }
         else{
             for (SanPham sp: danhsach){
                 sp.xuatthongtin();
             }
             System.out.println("Tong trung binh gia san pham: "+ df.format(giatb(danhsach)));
         }
    }

    //hàm xóa sp
    public static void timxoasp(List<SanPham> danhsach, String tencanxoa){
        boolean daxoa= danhsach.removeIf(sp -> sp.getten().equals(tencanxoa));
        if (daxoa) {
            System.out.println("San pham da xoa: "+ tencanxoa);
        }
        else{
            System.out.println("San pham khong ton tai: "+ tencanxoa);
        }
    }

    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        List<SanPham> danhsach=new ArrayList<>();

        //nhập sản phẩm
        nhapsanpham(scanner, danhsach);
        sapxephanghoa(danhsach);
        hienthisanpham(danhsach);

        while (!danhsach.isEmpty()) {
            System.out.println("Nhap ten san pham can xoa (hoac nhap QUIT de thoat): ");
            String tencanxoa= scanner.nextLine();
            if(tencanxoa.equalsIgnoreCase("QUIT")){
                break;
            }
           timxoasp(danhsach, tencanxoa);
            hienthisanpham(danhsach);
        }
        scanner.close();
    }
}
}
