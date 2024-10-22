import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.io.FileWriter;
import java.io.IOException;

class Transaksi {
    private double jumlah;
    private String jenis;
    private String deskripsi;
    private Date tanggal;
    private String kategori;

    public Transaksi(double jumlah, String jenis, String deskripsi, Date tanggal, String kategori) {
        this.jumlah = jumlah;
        this.jenis = jenis;
        this.deskripsi = deskripsi;
        this.tanggal = tanggal;
        this.kategori = kategori;
    }

    public double getJumlah() {
        return jumlah;
    }

    public String getJenis() {
        return jenis;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public String getKategori() {
        return kategori;
    }

    @Override
    public String toString() {
        SimpleDateFormat formatTanggal = new SimpleDateFormat("dd/MM/yyyy");
        return formatTanggal.format(tanggal) + " - " + jenis + " - " + deskripsi + ": " + jumlah;
    }
}

class Akun {
    private double saldo;
    private ArrayList<Transaksi> daftarTransaksi;
    private Scanner scanner = new Scanner(System.in);

    public Akun() {
        this.saldo = 0.0;
        this.daftarTransaksi = new ArrayList<>();
    }

    public double getSaldo() {
        return saldo;
    }

    public void tambahTransaksi(Transaksi transaksi) {
        if (transaksi.getJenis().equalsIgnoreCase("Pendapatan")) {
            saldo += transaksi.getJumlah();
        } else if (transaksi.getJenis().equalsIgnoreCase("Pengeluaran")) {
            saldo -= transaksi.getJumlah();
        }
        daftarTransaksi.add(transaksi);
    }

    public void tampilkanRiwayatTransaksi() {
        if (daftarTransaksi.isEmpty()) {
            System.out.println("Tidak ada transaksi.");
        } else {
            for (Transaksi t : daftarTransaksi) {
                System.out.println(t.toString());
            }
        }
    }

    public void tampilkanLaporan() {
        System.out.println("Saldo saat ini: " + saldo);
        double totalPendapatan = 0, totalPengeluaran = 0;
        for (Transaksi t : daftarTransaksi) {
            if (t.getJenis().equalsIgnoreCase("Pendapatan")) {
                totalPendapatan += t.getJumlah();
            } else if (t.getJenis().equalsIgnoreCase("Pengeluaran")) {
                totalPengeluaran += t.getJumlah();
            }
        }
        System.out.println("Total Pendapatan: " + totalPendapatan);
        System.out.println("Total Pengeluaran: " + totalPengeluaran);
    }

    public void filterTransaksi() {
        System.out.println("Filter Transaksi Berdasarkan:");
        System.out.println("1. Rentang Tanggal");
        System.out.println("2. Jenis Transaksi (Pendapatan/Pengeluaran)");
        System.out.println("3. Jumlah Transaksi");
        System.out.print("Pilih opsi filter: ");
        int filterOption = scanner.nextInt();
        scanner.nextLine();

        switch (filterOption) {
            case 1:
                    System.out.println("==========================================");
                    System.out.println("=> Filter Transaksi Berdasarkan Tanggal <=");
                    System.out.println("==========================================");
                System.out.print("Masukkan tanggal mulai (dd/MM/yyyy): ");
                String tanggalMulaiStr = scanner.nextLine();
                System.out.print("Masukkan tanggal akhir (dd/MM/yyyy): ");
                String tanggalAkhirStr = scanner.nextLine();
                try {
                    Date tanggalMulai = new SimpleDateFormat("dd/MM/yyyy").parse(tanggalMulaiStr);
                    Date tanggalAkhir = new SimpleDateFormat("dd/MM/yyyy").parse(tanggalAkhirStr);
                    daftarTransaksi.stream()
                            .filter(t -> !t.getTanggal().before(tanggalMulai) && !t.getTanggal().after(tanggalAkhir))
                            .forEach(System.out::println);
                } catch (Exception e) {
                    System.out.println("Format tanggal salah.");
                }
                break;
            case 2:
                    System.out.println("==================================================");
                    System.out.println("=> Filter Transaksi Berdasarkan Jenis Transaksi <=");
                    System.out.println("==================================================");
                System.out.print("Masukkan jenis transaksi (Pendapatan/Pengeluaran): ");
                String jenisTransaksi = scanner.nextLine();
                daftarTransaksi.stream()
                        .filter(t -> t.getJenis().equalsIgnoreCase(jenisTransaksi))
                        .forEach(System.out::println);
                break;
            case 3:
                    System.out.println("===================================================");
                    System.out.println("=> Filter Transaksi Berdasarkan Jumlah Transaksi <=");
                    System.out.println("===================================================");
                System.out.print("Masukkan jumlah minimum: ");
                double jumlahMin = scanner.nextDouble();
                System.out.print("Masukkan jumlah maksimum: ");
                double jumlahMax = scanner.nextDouble();
                daftarTransaksi.stream()
                        .filter(t -> t.getJumlah() >= jumlahMin && t.getJumlah() <= jumlahMax)
                        .forEach(System.out::println);
                break;
            default:
                System.out.println("Pilihan filter tidak valid.");
        }
    }

    public void tampilkanGrafikLaporan() {
        double totalPendapatan = 0, totalPengeluaran = 0;
        for (Transaksi t : daftarTransaksi) {
            if (t.getJenis().equalsIgnoreCase("Pendapatan")) {
                totalPendapatan += t.getJumlah();
            } else if (t.getJenis().equalsIgnoreCase("Pengeluaran")) {
                totalPengeluaran += t.getJumlah();
            }
        }
    
        if (totalPendapatan == 0) {
            System.out.println("Tidak ada pendapatan untuk menghitung grafik.");
            return;
        }
    
        double sisaPendapatan = totalPendapatan - totalPengeluaran;
    
        System.out.println("\nGrafik Komposisi Keuangan:");
        System.out.println("Pendapatan: " + (int) sisaPendapatan + " | " + barChart(sisaPendapatan, totalPendapatan));
        System.out.println("Pengeluaran: " + (int) totalPengeluaran + " | " + barChart(totalPengeluaran, totalPendapatan));
    }
    
    private String barChart(double value, double totalPendapatan) {
        int length = (int) Math.round((value / totalPendapatan) * 50);
        return "=".repeat(length) + " (" + (int) Math.round((value / totalPendapatan) * 100) + "%)";
    }
    
      
    

    public void exportData(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (Transaksi t : daftarTransaksi) {
                writer.write(t.getTanggal() + "," + t.getJenis() + "," + t.getDeskripsi() + "," + t.getJumlah() + "," + t.getKategori() + "\n");
            }
            System.out.println("Data berhasil diekspor ke " + fileName);
        } catch (IOException e) {
            System.out.println("Gagal mengekspor data: " + e.getMessage());
        }
    }

    public void tampilkanLaporanBulanan(int bulan, int tahun) {
        double totalPendapatan = 0, totalPengeluaran = 0;
        SimpleDateFormat formatBulan = new SimpleDateFormat("MM-yyyy");
        for (Transaksi t : daftarTransaksi) {
            if (formatBulan.format(t.getTanggal()).equals(String.format("%02d-%d", bulan, tahun))) {
                if (t.getJenis().equalsIgnoreCase("Pendapatan")) {
                    totalPendapatan += t.getJumlah();
                } else if (t.getJenis().equalsIgnoreCase("Pengeluaran")) {
                    totalPengeluaran += t.getJumlah();
                }
            }
        }
        System.out.println("Laporan Bulanan untuk " + bulan + "/" + tahun);
        System.out.println("Total Pendapatan: " + totalPendapatan);
        System.out.println("Total Pengeluaran: " + totalPengeluaran);
    }
}

class AkunBisnis extends Akun {
    private String namaBisnis;

    public AkunBisnis(String namaBisnis) {
        super();
        this.namaBisnis = namaBisnis;
    }

    public String getNamaBisnis() {
        return namaBisnis;
    }

    public void setNamaBisnis(String namaBisnis) {
        this.namaBisnis = namaBisnis;
    }

    @Override
    public void tampilkanLaporan() {
        System.out.println("Laporan Keuangan untuk Bisnis: " + namaBisnis);
        super.tampilkanLaporan();
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Akun akunPribadi = new Akun();

        boolean running = true;
        while (running) {
            System.out.println("Selamat datang di Aplikasi Keuangan Pribadi by Rahmat Deny");
            System.out.println("Silakan pilih jenis akun yang ingin digunakan:");
            System.out.println("1. Akun Pribadi");
            System.out.println("2. Akun Bisnis");
            System.out.println("3. Keluar");
            System.out.print("Pilihan: ");
            int pilihanAkun = scanner.nextInt();
            scanner.nextLine();

            switch (pilihanAkun) {
                case 1:
                    menuAkun(akunPribadi, scanner);
                    break;
                case 2:
                    System.out.print("Masukkan nama bisnis: ");
                    String namaBisnis = scanner.nextLine();
                    AkunBisnis akunBisnis = new AkunBisnis(namaBisnis);
                    menuAkun(akunBisnis, scanner);
                    break;
                case 3:
                    running = false;
                    System.out.println("Terimakasih telah menggunakan aplikasi Keuangan buatan Rahmat Deny.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    public static void menuAkun(Akun akun, Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("\nMenu:");
            System.out.println("1. Tambah Transaksi");
            System.out.println("2. Tampilkan Riwayat Transaksi");
            System.out.println("3. Tampilkan Laporan");
            System.out.println("4. Filter Transaksi");
            System.out.println("5. Tampilkan Grafik Laporan");
            System.out.println("6. Export Data");
            System.out.println("7. Tampilkan Laporan Bulanan");
            System.out.println("8. Kembali");
            System.out.print("Pilih menu: ");
            int menu = scanner.nextInt();
            scanner.nextLine();
            switch (menu) {
                case 1:
                    System.out.println("====================");
                    System.out.println("=> Tambah Transaksi <=");
                    System.out.println("====================");
                    tambahTransaksi(akun, scanner);
                    break;
                case 2:
                    System.out.println("=======================");
                    System.out.println("=> Riwayat Transaksi <=");
                    System.out.println("=======================");
                    akun.tampilkanRiwayatTransaksi();
                    break;
                case 3:
                    System.out.println("======================");
                    System.out.println("=> Laporan Keuangan <=");
                    System.out.println("======================");
                    akun.tampilkanLaporan();
                    break;
                case 4:
                    akun.filterTransaksi();
                    break;
                case 5:
                    System.out.println("====================");
                    System.out.println("=> Grafik Laporan <=");
                    System.out.println("====================");
                    akun.tampilkanGrafikLaporan();
                    break;
                case 6:
                    System.out.println("==========================");
                    System.out.println("=> Simpan File Laporan  <=");
                    System.out.println("==========================");
                    System.out.print("Masukkan nama file: ");
                    String fileName = scanner.nextLine();
                    akun.exportData(fileName);
                    break;
                case 7:
                    System.out.println("=====================");
                    System.out.println("=> Laporan Bulanan <=");
                    System.out.println("=====================");
                    System.out.print("Masukkan bulan (1-12): ");
                    int bulan = scanner.nextInt();
                    System.out.print("Masukkan tahun: ");
                    int tahun = scanner.nextInt();
                    akun.tampilkanLaporanBulanan(bulan, tahun);
                    break;
                case 8:
                    running = false;
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    public static void tambahTransaksi(Akun akun, Scanner scanner) {
        System.out.print("Masukkan jumlah: ");
        double jumlah = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Masukkan jenis transaksi (Pendapatan/Pengeluaran): ");
        String jenis = scanner.nextLine();
        System.out.print("Masukkan deskripsi: ");
        String deskripsi = scanner.nextLine();
        System.out.print("Masukkan kategori: ");
        String kategori = scanner.nextLine();
        Date tanggal = new Date();
        Transaksi transaksi = new Transaksi(jumlah, jenis, deskripsi, tanggal, kategori);
        akun.tambahTransaksi(transaksi);
        System.out.println("Transaksi berhasil ditambahkan.");
    }
}