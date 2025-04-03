import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BaiTapB6QLNV {

    static class NhanVien {
        protected String maNhanVien;
        protected String hoTen;
        protected int tuoi;
        protected String email;
        protected double luongCoBan;

        public NhanVien(String maNhanVien, String hoTen, int tuoi, String email, double luongCoBan) {
            this.maNhanVien = maNhanVien;
            this.hoTen = hoTen;
            this.tuoi = tuoi;
            this.email = email;
            this.luongCoBan = luongCoBan;
        }

        public double getThuNhap() {
            return luongCoBan;
        }

        @Override
        public String toString() {
            DecimalFormat df = new DecimalFormat("#,###");
            return "Ma NV: " + maNhanVien +
                    ", Ho Ten: " + hoTen +
                    ", Tuoi: " + tuoi +
                    ", Email: " + email +
                    ", Thu Nhap: " + df.format(getThuNhap());
        }

        public String[] toTableRow() {
            DecimalFormat df = new DecimalFormat("#,###");
            return new String[]{
                    maNhanVien, hoTen, String.valueOf(tuoi), email, df.format(luongCoBan)
            };
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Danh sách nhân viên
            List<NhanVien> danhSachNhanVien = new ArrayList<>();

            // Tạo cửa sổ chính với layout null
            JFrame frame = new JFrame("Quản Lý Nhân Viên");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 700);
            frame.setLayout(null);

            // Tiêu đề và đồng hồ hiển thị
            JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN VIÊN", SwingConstants.CENTER);
            lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
            lblTitle.setForeground(Color.RED);
            lblTitle.setBounds(300, 10, 300, 40);
            frame.add(lblTitle);

            JLabel lblTime = new JLabel();
            lblTime.setFont(new Font("Arial", Font.BOLD, 14));
            lblTime.setForeground(Color.RED);
            lblTime.setBounds(800, 10, 100, 30);
            frame.add(lblTime);
            new Timer(1000, e -> lblTime.setText(new SimpleDateFormat("hh:mm a").format(new Date()))).start();

            // Các trường nhập liệu (Form)
            int startY = 60;
            int labelWidth = 100, fieldWidth = 250, height = 30, gapY = 40;

            JLabel lblMaNV = new JLabel("Mã NV:");
            lblMaNV.setBounds(20, startY, labelWidth, height);
            frame.add(lblMaNV);
            JTextField tfMaNV = new JTextField();
            tfMaNV.setBounds(130, startY, fieldWidth, height);
            frame.add(tfMaNV);

            JLabel lblHoTen = new JLabel("Họ Tên:");
            lblHoTen.setBounds(20, startY + gapY, labelWidth, height);
            frame.add(lblHoTen);
            JTextField tfHoTen = new JTextField();
            tfHoTen.setBounds(130, startY + gapY, fieldWidth, height);
            frame.add(tfHoTen);

            JLabel lblTuoi = new JLabel("Tuổi:");
            lblTuoi.setBounds(20, startY + 2 * gapY, labelWidth, height);
            frame.add(lblTuoi);
            JTextField tfTuoi = new JTextField();
            tfTuoi.setBounds(130, startY + 2 * gapY, fieldWidth, height);
            frame.add(tfTuoi);

            JLabel lblEmail = new JLabel("Email:");
            lblEmail.setBounds(20, startY + 3 * gapY, labelWidth, height);
            frame.add(lblEmail);
            JTextField tfEmail = new JTextField();
            tfEmail.setBounds(130, startY + 3 * gapY, fieldWidth, height);
            frame.add(tfEmail);

            JLabel lblLuong = new JLabel("Lương:");
            lblLuong.setBounds(20, startY + 4 * gapY, labelWidth, height);
            frame.add(lblLuong);
            JTextField tfLuong = new JTextField();
            tfLuong.setBounds(130, startY + 4 * gapY, fieldWidth, height);
            frame.add(tfLuong);

            // Các nút chức năng (bố trí theo cột bên phải)
            int btnX = 450, btnY = 60, btnWidth = 150, btnHeight = 30, btnGap = 40;

            JButton btnThem = new JButton("Thêm nhân viên");
            btnThem.setBounds(btnX, btnY, btnWidth, btnHeight);
            frame.add(btnThem);

            JButton btnXuatDS = new JButton("Xuất danh sách");
            btnXuatDS.setBounds(btnX, btnY + btnGap, btnWidth, btnHeight);
            frame.add(btnXuatDS);

            JButton btnXoa = new JButton("Xóa theo mã");
            btnXoa.setBounds(btnX, btnY + 2 * btnGap, btnWidth, btnHeight);
            frame.add(btnXoa);

            JButton btnCapNhat = new JButton("Cập nhật");
            btnCapNhat.setBounds(btnX, btnY + 3 * btnGap, btnWidth, btnHeight);
            frame.add(btnCapNhat);

            JButton btnTimKiem = new JButton("Tìm theo mã");
            btnTimKiem.setBounds(btnX, btnY + 4 * btnGap, btnWidth, btnHeight);
            frame.add(btnTimKiem);

            // Bảng hiển thị danh sách nhân viên
            DefaultTableModel tableModel = new DefaultTableModel(
                    new Object[]{"Mã NV", "Họ Tên", "Tuổi", "Email", "Lương (VND)"}, 0);
            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBounds(20, startY + 5 * gapY + 20, 850, 300);
            frame.add(scrollPane);

            // Sự kiện nút "Thêm nhân viên"
            btnThem.addActionListener(e -> {
                String maNV = tfMaNV.getText().trim();
                String hoTen = tfHoTen.getText().trim();
                String tuoiStr = tfTuoi.getText().trim();
                String email = tfEmail.getText().trim();
                String luongStr = tfLuong.getText().trim();

                if (maNV.isEmpty() || hoTen.isEmpty() || tuoiStr.isEmpty() || email.isEmpty() || luongStr.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Vui lòng nhập đủ thông tin!");
                    return;
                }

                if (!maNV.matches("\\d{3}+")) {
                    JOptionPane.showMessageDialog(frame, "Mã nhân viên phải là 3 chữ số!");
                    return;
                }
                for (NhanVien nv : danhSachNhanVien) {
                    if (nv.maNhanVien.equals(maNV)) {
                        JOptionPane.showMessageDialog(frame, "Mã nhân viên không được trùng!");
                        return;
                    }
                }

                if (!hoTen.matches("[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯưẠ-ỹ\\s]+")) {
                    JOptionPane.showMessageDialog(frame, "Họ tên chỉ được chứa chữ cái và khoảng trắng!");
                    return;
                }

                if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                    JOptionPane.showMessageDialog(frame, "Email phải đúng định dạng!");
                    return;
                }

                try {
                    int tuoi = Integer.parseInt(tuoiStr);
                    if (tuoi < 0) {
                        JOptionPane.showMessageDialog(frame, "Tuổi không thể là số âm!");
                        return;
                    }
                    double luong = Double.parseDouble(luongStr);

                    NhanVien nv = new NhanVien(maNV, hoTen, tuoi, email, luong);
                    danhSachNhanVien.add(nv);
                    tableModel.addRow(nv.toTableRow());

                    JOptionPane.showMessageDialog(frame, "Thêm nhân viên thành công!");
                    tfMaNV.setText("");
                    tfHoTen.setText("");
                    tfTuoi.setText("");
                    tfEmail.setText("");
                    tfLuong.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Tuổi và lương phải là số hợp lệ!");
                }
            });

            // Sự kiện nút "Xuất danh sách"
            btnXuatDS.addActionListener(e -> {
                if (danhSachNhanVien.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Không có dữ liệu để xuất!");
                    return;
                }
                try {
                    File file = new File("DanhSachNhanVien.txt");
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));

                    for (NhanVien nv : danhSachNhanVien) {
                        writer.write(nv.toString());
                        writer.newLine();
                    }
                    writer.close();
                    JOptionPane.showMessageDialog(frame, "Danh sách đã được xuất ra file: DanhSachNhanVien.txt");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Không thể ghi ra file!");
                }
            });

            // Sự kiện nút "Xóa theo mã"
            btnXoa.addActionListener(e -> {
                String maNV = JOptionPane.showInputDialog(frame, "Nhập mã nhân viên cần xóa:");
                if (maNV != null) {
                    boolean removed = danhSachNhanVien.removeIf(nv -> nv.maNhanVien.equals(maNV));
                    if (removed) {
                        tableModel.setRowCount(0);
                        for (NhanVien nv : danhSachNhanVien) {
                            tableModel.addRow(nv.toTableRow());
                        }
                        JOptionPane.showMessageDialog(frame, "Đã xóa nhân viên có mã " + maNV);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Không tìm thấy nhân viên có mã " + maNV);
                    }
                }
            });

            // Sự kiện nút "Cập nhật"
            btnCapNhat.addActionListener(e -> {
                DecimalFormat df = new DecimalFormat("#,###");
                String maNV = JOptionPane.showInputDialog(frame, "Nhập mã nhân viên cần cập nhật:");
                if (maNV != null) {
                    for (NhanVien nv : danhSachNhanVien) {
                        if (nv.maNhanVien.equals(maNV)) {
                            JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

                            JTextField tfHoTenMoi = new JTextField(nv.hoTen);
                            JTextField tfTuoiMoi = new JTextField(String.valueOf(nv.tuoi));
                            JTextField tfEmailMoi = new JTextField(nv.email);

                            NumberFormat numberFormat = NumberFormat.getNumberInstance();
                            JFormattedTextField tfLuongMoi = new JFormattedTextField(numberFormat);
                            tfLuongMoi.setValue(nv.luongCoBan);


                            panel.add(new JLabel("Họ Tên mới:"));
                            panel.add(tfHoTenMoi);
                            panel.add(new JLabel("Tuổi mới:"));
                            panel.add(tfTuoiMoi);
                            panel.add(new JLabel("Email mới:"));
                            panel.add(tfEmailMoi);
                            panel.add(new JLabel("Lương mới:"));
                            panel.add(tfLuongMoi);

                            int result = JOptionPane.showConfirmDialog(frame, panel, "Cập nhật thông tin", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                            if (result == JOptionPane.OK_OPTION) {
                                try {
                                    String hoTenMoi = tfHoTenMoi.getText().trim();
                                    String tuoiMoiStr = tfTuoiMoi.getText().trim();
                                    String emailMoi = tfEmailMoi.getText().trim();
                                    String luongMoiStr = tfLuongMoi.getText().trim().replace(",", ""); // Loại bỏ dấu phẩy trước khi chuyển đổi

                                    if (!hoTenMoi.matches("[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯưẠ-ỹ\\s]+")) {
                                        JOptionPane.showMessageDialog(frame, "Họ tên chỉ được chứa chữ cái và khoảng trắng!");
                                        return;
                                    }

                                    if (!emailMoi.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                                        JOptionPane.showMessageDialog(frame, "Email phải đúng định dạng!");
                                        return;
                                    }

                                    int tuoiMoi = Integer.parseInt(tuoiMoiStr);
                                    double luongMoi = ((Number) tfLuongMoi.getValue()).doubleValue();

                                    nv.hoTen = hoTenMoi;
                                    nv.tuoi = tuoiMoi;
                                    nv.email = emailMoi;
                                    nv.luongCoBan = luongMoi;

                                    tableModel.setRowCount(0);
                                    for (NhanVien n : danhSachNhanVien) {
                                        tableModel.addRow(n.toTableRow());
                                    }
                                    JOptionPane.showMessageDialog(frame, "Cập nhật thông tin thành công!");
                                } catch (NumberFormatException ex) {
                                    JOptionPane.showMessageDialog(frame, "Tuổi và lương phải là số hợp lệ!");
                                }
                            }
                            return;
                        }
                    }
                    JOptionPane.showMessageDialog(frame, "Không tìm thấy nhân viên có mã " + maNV);
                }
            });


            // Sự kiện nút "Tìm theo mã"
            btnTimKiem.addActionListener(e -> {
                String maNV = JOptionPane.showInputDialog(frame, "Nhập mã nhân viên cần tìm:");
                if (maNV != null) {
                    for (NhanVien nv : danhSachNhanVien) {
                        if (nv.maNhanVien.equals(maNV)) {
                            JOptionPane.showMessageDialog(frame, "Tìm thấy: " + nv);
                            return;
                        }
                    }
                    JOptionPane.showMessageDialog(frame, "Không tìm thấy nhân viên có mã " + maNV);
                }
            });

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
