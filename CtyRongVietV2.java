import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class CtyRongVietV2 {
    static abstract class NhanVienCty {
        protected String hoten;
        protected String maNhanVien;

        public NhanVienCty(String maNhanVien, String hoten) {
            this.maNhanVien = maNhanVien;
            this.hoten = hoten;
        }

        public abstract double getThuNhap();

        @Override
        public String toString() {
            DecimalFormat df = new DecimalFormat("#,###");
            return "Ma NV: " + maNhanVien + ", Ho Ten: " + hoten + ", Thu Nhap: " + df.format(getThuNhap());
        }
    }

    static class NhanVienHanhChinh extends NhanVienCty {
        private double luongCoBan;
        private int ngayCong;

        public NhanVienHanhChinh(String maNhanVien, String hoten, double luongCoBan, int ngayCong) {
            super(maNhanVien, hoten);
            this.luongCoBan = luongCoBan;
            this.ngayCong = ngayCong;
        }

        @Override
        public double getThuNhap() {
            return luongCoBan * ngayCong;
        }
    }

    static class NhanVienKinhDoanh extends NhanVienCty {
        private double luongCoBan;
        private double luongKinhDoanh;

        public NhanVienKinhDoanh(String maNhanVien, String hoten, double luongCoBan, double luongKinhDoanh) {
            super(maNhanVien, hoten);
            this.luongCoBan = luongCoBan;
            this.luongKinhDoanh = luongKinhDoanh;
        }

        @Override
        public double getThuNhap() {
            return luongCoBan + luongKinhDoanh;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Tạo cửa sổ chính
            JFrame frame = new JFrame("Quản Lý Nhân Viên Cty Rồng Việt");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 700);

            List<NhanVienCty> danhSachNhanVien = new ArrayList<>();
            double luongCoBan = 800000;

            // GIAO DIỆN CHÍNH
            JPanel panel = new JPanel(new BorderLayout(10, 10));

            // Bảng hiển thị danh sách nhân viên
            JTable table = new JTable();
            DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Mã NV", "Họ Tên", "Thu Nhập"}, 0);
            table.setModel(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);

            // Form nhập dữ liệu nhân viên
            JPanel inputPanel = new JPanel(new GridLayout(2, 5, 10, 10));
            JTextField tfMaNV = new JTextField();
            JTextField tfHoTen = new JTextField();
            JTextField tfNgayCong = new JTextField();
            JTextField tfLuongKinhDoanh = new JTextField();
            JButton btnThem = new JButton("Thêm NV");

            tfMaNV.addCaretListener(e -> {
                String maNV = tfMaNV.getText().trim();
                if (maNV.startsWith("1")){
                    tfNgayCong.setEnabled(true);
                    tfLuongKinhDoanh.setEnabled(false);
                    tfHoTen.setEnabled(true);
                }
                else if (maNV.startsWith("2")){
                    tfLuongKinhDoanh.setEnabled(true);
                    tfNgayCong.setEnabled(false);
                    tfHoTen.setEnabled(true);
                }
                else {
                    tfNgayCong.setEnabled(false);
                    tfLuongKinhDoanh.setEnabled(false);
                    tfHoTen.setEnabled(false);
                }
            });

            inputPanel.add(new JLabel("Mã NV:"));
            inputPanel.add(tfMaNV);
            inputPanel.add(new JLabel("Họ Tên:"));
            inputPanel.add(tfHoTen);
            inputPanel.add(btnThem);

            inputPanel.add(new JLabel("Ngày Công:"));
            inputPanel.add(tfNgayCong);
            inputPanel.add(new JLabel("Lương Kinh Doanh:"));
            inputPanel.add(tfLuongKinhDoanh);

            panel.add(inputPanel, BorderLayout.NORTH);

            // Các nút thực hiện chức năng
            JPanel buttonPanel = new JPanel(new GridLayout(2, 4, 10, 10)); // 2 hàng, 4 cột, khoảng cách 10px
            JButton btnXuatDS = new JButton("Xuất Danh Sách");
            JButton btnTimKiem = new JButton("Tìm Kiếm Theo Mã");
            JButton btnTimKiemLuong = new JButton("Tìm Kiếm Theo Lương");
            JButton btnXoa = new JButton("Xóa Theo Mã");
            JButton btnCapNhat = new JButton("Cập Nhật");
            JButton btnTop5 = new JButton("Top 5 Thu Nhập Cao");
            JButton btnSapXepTen = new JButton("Sắp Xếp Theo Tên");
            JButton btnSapXepThuNhap = new JButton("Sắp Xếp Theo Thu Nhập");
            

            buttonPanel.add(btnXuatDS);
            buttonPanel.add(btnTimKiem);
            buttonPanel.add(btnTimKiemLuong);
            buttonPanel.add(btnXoa);
            buttonPanel.add(btnCapNhat);
            buttonPanel.add(btnTop5);
            buttonPanel.add(btnSapXepTen);
            buttonPanel.add(btnSapXepThuNhap);


            panel.add(buttonPanel, BorderLayout.SOUTH);
            

            // Thêm nhân viên
            btnThem.addActionListener(e -> {
                String maNV = tfMaNV.getText().trim();
                String hoTen = tfHoTen.getText().trim();

                tfNgayCong.setEnabled(maNV.startsWith("1"));
                tfLuongKinhDoanh.setEnabled(maNV.startsWith("2"));

                if (maNV.isEmpty() || hoTen.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Mã NV và Họ Tên không được bỏ trống!");
                    return;
                }

                if (maNV.startsWith("1")) { // Nhân viên hành chính
                    try {
                        int ngayCong = Integer.parseInt(tfNgayCong.getText().trim());
                        danhSachNhanVien.add(new NhanVienHanhChinh(maNV, hoTen, luongCoBan, ngayCong));
                        JOptionPane.showMessageDialog(frame, "Thêm nhân viên hành chính thành công!");
                        tfMaNV.setText("");
                        tfHoTen.setText("");
                        tfNgayCong.setText("");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Ngày công phải là số nguyên!");
                    }
                } else if (maNV.startsWith("2")) { // Nhân viên kinh doanh
                    try {
                        double luongKD = Double.parseDouble(tfLuongKinhDoanh.getText().trim());
                        danhSachNhanVien.add(new NhanVienKinhDoanh(maNV, hoTen, luongCoBan, luongKD));
                        JOptionPane.showMessageDialog(frame, "Thêm nhân viên kinh doanh thành công!");
                        tfMaNV.setText("");
                        tfHoTen.setText("");
                        tfLuongKinhDoanh.setText("");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Lương kinh doanh phải là số thực!");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Mã nhân viên không hợp lệ (phải bắt đầu bằng 1 hoặc 2)!");
                }
            });

            // Xuất danh sách nhân viên
            btnXuatDS.addActionListener(e -> {
                tableModel.setRowCount(0); // Xóa toàn bộ dữ liệu cũ
                for (NhanVienCty nv : danhSachNhanVien) {
                    tableModel.addRow(new Object[]{nv.maNhanVien, nv.hoten, new DecimalFormat("#,###").format(nv.getThuNhap())});
                }
            });

            // Tìm kiếm theo mã
            btnTimKiem.addActionListener(e -> {
                String maNV = JOptionPane.showInputDialog(frame, "Nhập mã nhân viên cần tìm:");
                for (NhanVienCty nv : danhSachNhanVien) {
                    if (nv.maNhanVien.equals(maNV)) {
                        JOptionPane.showMessageDialog(frame, "Tìm thấy: " + nv);
                        return;
                    }
                }
                JOptionPane.showMessageDialog(frame, "Không tìm thấy nhân viên có mã " + maNV);
            });

            // Tìm kiếm theo lương
            btnTimKiemLuong.addActionListener(e -> {
                DecimalFormat df = new DecimalFormat("#,###");
                try {
                    double timLuong = Double.parseDouble(JOptionPane.showInputDialog(frame, "Nhập lương nhân viên cần tìm:"));
                    boolean found = false;
                    for (NhanVienCty nv : danhSachNhanVien) {
                        if (nv.getThuNhap() == timLuong) {
                            JOptionPane.showMessageDialog(frame, "Tìm thấy: " + nv);
                            found = true;
                        }
                    }
                    if (!found) {
                        JOptionPane.showMessageDialog(frame, "Không tìm thấy nhân viên có lương bằng " + df.format(timLuong));
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Vui lòng nhập một số hợp lệ!");
                }
            });

            // Xóa nhân viên theo mã
            btnXoa.addActionListener(e -> {
                String maNV = JOptionPane.showInputDialog(frame, "Nhập mã nhân viên cần xóa:");
                danhSachNhanVien.removeIf(nv -> nv.maNhanVien.equals(maNV));
                JOptionPane.showMessageDialog(frame, "Đã xóa nhân viên (nếu có).");
            });

            // Cập nhật thông tin nhân viên
            btnCapNhat.addActionListener(e -> {
                String maNV = JOptionPane.showInputDialog(frame, "Nhập mã nhân viên cần cập nhật:");
                for (NhanVienCty nv : danhSachNhanVien) {
                    if (nv.maNhanVien.equals(maNV)) {
                        String hoTenMoi = JOptionPane.showInputDialog(frame, "Nhập họ tên mới:");
                        nv.hoten = hoTenMoi;
                        JOptionPane.showMessageDialog(frame, "Cập nhật thành công!");
                        return;
                    }
                }
                JOptionPane.showMessageDialog(frame, "Không tìm thấy nhân viên có mã " + maNV);
            });

            // Sắp xếp theo tên
            btnSapXepTen.addActionListener(e -> {
                danhSachNhanVien.sort(Comparator.comparing(nv -> nv.hoten));
                tableModel.setRowCount(0); // Xóa toàn bộ dữ liệu cũ
                for (NhanVienCty nv : danhSachNhanVien) {
                    tableModel.addRow(new Object[]{nv.maNhanVien, nv.hoten, new DecimalFormat("#,###").format(nv.getThuNhap())});
                }
                JOptionPane.showMessageDialog(frame, "Đã sắp xếp danh sách theo tên.");
            });

            // Sắp xếp theo thu nhập
            btnSapXepThuNhap.addActionListener(e -> {
                danhSachNhanVien.sort(Comparator.comparingDouble(NhanVienCty::getThuNhap).reversed());
                tableModel.setRowCount(0); // Xóa toàn bộ dữ liệu cũ

                for (NhanVienCty nv : danhSachNhanVien) {
                    tableModel.addRow(new Object[]{nv.maNhanVien, nv.hoten, new DecimalFormat("#,###").format(nv.getThuNhap())});
                }
                JOptionPane.showMessageDialog(frame, "Đã sắp xếp danh sách theo thu nhập.");
            });

            // Xuất top 5 nhân viên có thu nhập cao nhất
            btnTop5.addActionListener(e -> {
                danhSachNhanVien.sort(Comparator.comparingDouble(NhanVienCty::getThuNhap).reversed());
                StringBuilder sb = new StringBuilder("Top 5 nhân viên có thu nhập cao nhất:\n");
                for (int i = 0; i < Math.min(5, danhSachNhanVien.size()); i++) {
                    sb.append(danhSachNhanVien.get(i)).append("\n");
                }
                JOptionPane.showMessageDialog(frame, sb.toString());
            });



            frame.add(panel);
            frame.setVisible(true);
        });
    }
}