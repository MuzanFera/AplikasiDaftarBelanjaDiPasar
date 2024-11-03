import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Kelas utama untuk aplikasi daftar belanja di pasar
public class AplikasiDaftarBelanjaPasar {
    public static void main(String[] args) {
        // Membuat frame utama aplikasi
        JFrame frame = new JFrame("Aplikasi Daftar Belanja Pasar");
        frame.setSize(800, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Membuat menu bar dan menu
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem addItemMenuItem = new JMenuItem("Tambah Item Belanja"); // Menu untuk menambah item belanja
        menu.add(addItemMenuItem);
        frame.setJMenuBar(menuBar);
        menuBar.add(menu);

        // Panel utama untuk menampilkan daftar belanja
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Tabel untuk menampilkan daftar belanja
        String[] columnNames = {"Item", "Deskripsi", "Jumlah", "Satuan", "Kategori", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);

        // Menambahkan tabel ke dalam panel utama
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Membuat DefaultListModel untuk JList
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> itemList = new JList<>(listModel);
        JScrollPane listScrollPane = new JScrollPane(itemList);
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Menambahkan JList ke panel utama di sebelah kanan tabel
        mainPanel.add(listScrollPane, BorderLayout.EAST);
        listScrollPane.setPreferredSize(new Dimension(150, 0)); // Mengatur lebar JList

        // Tombol untuk mengubah status item belanja
        JButton changeStatusButton = new JButton("Ubah Status");
        changeStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow(); // Mendapatkan baris yang dipilih
                if (selectedRow != -1) { // Pastikan ada baris yang dipilih
                    String currentStatus = (String) tableModel.getValueAt(selectedRow, 5); // Mendapatkan status saat ini
                    String newStatus = currentStatus.equals("Belum Dibeli") ? "Dibeli" : "Belum Dibeli";
                    tableModel.setValueAt(newStatus, selectedRow, 5); // Mengubah status di tabel
                } else {
                    // JOption ini digunakan untuk menampilkan pop up
                    JOptionPane.showMessageDialog(frame, "Silakan pilih item untuk mengubah status!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Tombol untuk menghapus item yang dipilih dari tabel
        JButton removeButton = new JButton("Hapus Item Terpilih");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = itemList.getSelectedIndex(); // Mendapatkan indeks item yang dipilih
                if (selectedIndex != -1) {
                    String selectedItem = listModel.get(selectedIndex); // Mendapatkan item yang dipilih
                    // Mencari item di tabel untuk dihapus
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        if (tableModel.getValueAt(i, 0).equals(selectedItem)) {
                            tableModel.removeRow(i); // Menghapus baris dari tabel
                            break;
                        }
                    }
                    listModel.remove(selectedIndex); // Menghapus item dari JList
                } else {
                    // JOption ini digunakan untuk menampilkan pop up
                    JOptionPane.showMessageDialog(frame, "Silakan pilih item dari daftar untuk dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Menambahkan tombol ke panel utama
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(changeStatusButton);
        buttonPanel.add(removeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Event listener untuk menampilkan form input saat menu "Tambah Item Belanja" diklik
        addItemMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Membuka dialog form input item belanja baru
                tampilkanFormTambahItem(tableModel, listModel);
            }
        });

        // Menambahkan panel utama ke dalam frame
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    // Metode untuk menampilkan form input item belanja
    private static void tampilkanFormTambahItem(DefaultTableModel tableModel, DefaultListModel<String> listModel) {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Menambahkan padding

        // Komponen input untuk nama item
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nama Item:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField itemNameField = new JTextField();
        formPanel.add(itemNameField, gbc);

        // Komponen input untuk deskripsi item menggunakan JTextArea
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Deskripsi:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JTextArea descriptionArea = new JTextArea(3, 20); // 3 baris, 20 kolom
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea); // Membuat scroll pane untuk JTextArea
        formPanel.add(scrollPane, gbc);

        // Komponen input untuk jumlah item menggunakan JSpinner
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Jumlah:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1)); // Spinner untuk angka, default 1, min 1, max 1000
        formPanel.add(quantitySpinner, gbc);

        // Komponen untuk memilih satuan jumlah
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Satuan:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        JPanel unitPanel = new JPanel();
        JRadioButton pcsRadioButton = new JRadioButton("Pcs");
        JRadioButton gramRadioButton = new JRadioButton("Gram");
        JRadioButton kgRadioButton = new JRadioButton("Kg");
        ButtonGroup unitGroup = new ButtonGroup();
        unitGroup.add(pcsRadioButton);
        unitGroup.add(gramRadioButton);
        unitGroup.add(kgRadioButton);
        pcsRadioButton.setSelected(true); // Set default ke pcs
        unitPanel.add(pcsRadioButton);
        unitPanel.add(gramRadioButton);
        unitPanel.add(kgRadioButton);
        formPanel.add(unitPanel, gbc);

        // Komponen dropdown untuk memilih kategori
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Kategori:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        String[] categories = {"Sayuran", "Buah", "Daging", "Ikan", "Lainnya"};
        JComboBox<String> categoryComboBox = new JComboBox<>(categories);
        formPanel.add(categoryComboBox, gbc);

        // Status item belanja default
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Status:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Belum Dibeli"), gbc); // Status default

        // JCheckBox untuk konfirmasi
        gbc.gridx = 0;
        gbc.gridy = 6;
        JCheckBox confirmCheckBox = new JCheckBox("Saya yakin ingin menambahkan item ini");
        formPanel.add(confirmCheckBox, gbc);

        // Menampilkan dialog untuk menambah item belanja
        int result = JOptionPane.showConfirmDialog(null, formPanel, "Tambah Item Belanja", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // Jika pengguna menekan OK dan sudah mencentang JCheckBox, data akan ditambahkan ke tabel
        if (result == JOptionPane.OK_OPTION && confirmCheckBox.isSelected()) {
            String itemName = itemNameField.getText();
            String description = descriptionArea.getText(); // Mendapatkan deskripsi dari JTextArea
            int quantity = (int) quantitySpinner.getValue(); // Mendapatkan nilai dari JSpinner
            String unit = pcsRadioButton.isSelected() ? "Pcs" : gramRadioButton.isSelected() ? "Gram" : "Kg"; // Mendapatkan satuan
            String category = (String) categoryComboBox.getSelectedItem();
            String status = "Belum Dibeli"; // Status default saat item ditambahkan

            // Menambahkan data item baru ke dalam tabel
            tableModel.addRow(new Object[]{itemName, description, quantity, unit, category, status});
            // Menambahkan item ke dalam JList
            listModel.addElement(itemName);
        } else if (result == JOptionPane.OK_OPTION) {
            // JOption ini digunakan untuk menampilkan pop up
            JOptionPane.showMessageDialog(null, "Silakan centang konfirmasi sebelum menambahkan item.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }
}
