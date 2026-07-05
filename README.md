<div align="center">

# 🏠 KosKosan

### Sistem Informasi Manajemen Kos Berbasis Objek dengan Integrasi Database

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/GUI-Java%20Swing-blue?style=for-the-badge)
![SQLite](https://img.shields.io/badge/Database-SQLite-003B57?style=for-the-badge&logo=sqlite&logoColor=white)
![NetBeans](https://img.shields.io/badge/IDE-NetBeans-1B6AC6?style=for-the-badge&logo=apache-netbeans-ide&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

*Aplikasi desktop untuk mengelola data kamar, penghuni, dan transaksi pembayaran kos secara terkomputerisasi.*

</div>

---

## 👥 Anggota Kelompok

| Nama | NIM | Peran |
|---|---|---|
| **Heru Chandra** | **2410501094** | Merancang model & struktur OOP (Kamar, KamarAC, KamarStandard, Penghuni, Transaksi, interface, dan exception) |
| **Muhammad Farrel Fauzan** | **2410501092** | Melakukan pengujian aplikasi & menyusun dokumentasi/laporan |
| **Radinka Alifasya Dinova** | **2410501073** | Membangun antarmuka pengguna (GUI) dengan Java Swing (MainFrame, PanelKamar, PanelPenghuni, PanelTransaksi) |
| **Tebing Rizky Tsaniansyah** | **2410501080** | Membangun koneksi & akses database (DatabaseConnection, KamarDAO, PenghuniDAO, TransaksiDAO) menggunakan SQLite |

---

## 📖 Tentang Project

**KosKosan** adalah aplikasi manajemen rumah kos berbasis Java Swing yang dibangun dengan menerapkan prinsip **Object Oriented Programming (OOP)** secara menyeluruh, dipadukan dengan **database SQLite** sebagai penyimpanan data yang persisten. Project ini dikembangkan sebagai bagian dari tugas mata kuliah Pemrograman Berorientasi Objek.

Aplikasi ini menggantikan pencatatan manual data kos dengan sistem yang lebih terstruktur, sehingga pengelola dapat memantau ketersediaan kamar, riwayat penghuni, dan status pembayaran sewa dengan lebih mudah dan akurat.

---

## ✨ Fitur Utama

| Fitur | Deskripsi |
|---|---|
| 🛏️ **Manajemen Kamar** | Menampilkan daftar kamar, tipe (Standard/AC), harga sewa, dan status ketersediaan |
| 👤 **Manajemen Penghuni** | Mencatat data penghuni: nama, NIK, no. telepon, kamar, tanggal masuk & keluar |
| 💳 **Manajemen Transaksi** | Mencatat transaksi pembayaran sewa beserta status (Lunas / Belum Lunas / Cicilan) |
| 💾 **Persistensi Data** | Seluruh data tersimpan permanen di database SQLite melalui JDBC |

---

## 🧩 Konsep OOP yang Diterapkan

<table>
<tr><td width="30%"><b>Abstraksi</b></td><td>Kelas <code>Kamar</code> bersifat <code>abstract</code>, mendefinisikan kontrak umum tanpa implementasi harga/deskripsi</td></tr>
<tr><td><b>Pewarisan (Inheritance)</b></td><td><code>KamarStandard</code> dan <code>KamarAC</code> mewarisi <code>Kamar</code></td></tr>
<tr><td><b>Polimorfisme</b></td><td>Method <code>getHarga()</code> & <code>getDescription()</code> di-<i>override</i> berbeda pada tiap kelas turunan</td></tr>
<tr><td><b>Enkapsulasi</b></td><td>Seluruh atribut model bersifat <code>private</code>, diakses lewat getter/setter</td></tr>
<tr><td><b>Interface</b></td><td><code>Bookable</code> (pesan/kosongkan kamar) & <code>Reportable</code> (generate laporan)</td></tr>
<tr><td><b>Custom Exception</b></td><td><code>KamarPenuhException</code>, <code>PembayaranGagalException</code>, <code>PenghuniTidakDitemukanException</code></td></tr>
<tr><td><b>Enum</b></td><td><code>TipeKamar</code> (STANDARD, AC) & <code>StatusTransaksi</code> (LUNAS, BELUM_LUNAS, CICILAN)</td></tr>
<tr><td><b>DAO Pattern</b></td><td>Pemisahan akses database dari logika model & tampilan</td></tr>
</table>

---

## 🗂️ Struktur Project

```
koskosan/
├── koskosan/
│   └── Main.java                          # Entry point aplikasi
│
├── koskosan.model/
│   ├── Kamar.java                         # Abstract class
│   ├── KamarAC.java                       # Turunan Kamar
│   ├── KamarStandard.java                 # Turunan Kamar
│   ├── Penghuni.java
│   └── Transaksi.java
│
├── koskosan.interface_/
│   ├── Bookable.java                      # Kontrak pemesanan kamar
│   └── Reportable.java                    # Kontrak pembuatan laporan
│
├── koskosan.exception/
│   ├── KamarPenuhException.java
│   ├── PembayaranGagalException.java
│   └── PenghuniTidakDitemukanException.java
│
├── koskosan.dao/
│   ├── DatabaseConnection.java            # Koneksi SQLite (JDBC)
│   ├── KamarDAO.java
│   ├── PenghuniDAO.java
│   └── TransaksiDAO.java
│
└── koskosan.gui/
    ├── MainFrame.java                     # Jendela utama aplikasi
    ├── PanelKamar.java
    ├── PanelKamarBeanInfo.java
    ├── PanelPenghuni.java
    └── PanelTransaksi.java
```

---

## 🛠️ Teknologi yang Digunakan

- **Bahasa:** Java (JDK 23)
- **GUI:** Java Swing
- **Database:** SQLite (via `sqlite-jdbc-3.36.0.jar`)
- **IDE:** Apache NetBeans

---

## 🚀 Cara Menjalankan

1. **Clone repository ini**
   ```bash
   git clone https://github.com/username/koskosan.git
   ```
2. **Buka project di NetBeans**
   - `File` → `Open Project` → pilih folder `koskosan`
3. **Pastikan library `sqlite-jdbc-3.36.0.jar` sudah ditambahkan** ke folder `Libraries` project (sudah termasuk dalam repo ini)
4. **Jalankan aplikasi**
   - Klik kanan pada `Main.java` → `Run File`, atau tekan `Shift + F6`

> 💡 Database SQLite akan otomatis dibuat/terhubung saat aplikasi pertama kali dijalankan melalui `DatabaseConnection.java`.

---

## 📸 Tampilan Aplikasi

<img width="1237" height="840" alt="image" src="https://github.com/user-attachments/assets/76c17430-a83c-43b8-96a8-36843582c282" />

<img width="1235" height="837" alt="image" src="https://github.com/user-attachments/assets/5878c8c0-83a1-4261-8614-dd8ec4fb207c" />

<img width="1231" height="837" alt="image" src="https://github.com/user-attachments/assets/9f53d639-0c30-4029-83aa-367b63bd1002" />

---

## 📄 Lisensi

Project ini dibuat untuk keperluan akademik (Tugas Akhir Mata Kuliah Pemrograman Berorientasi Objek).

<div align="center">

*Dibuat dengan ☕ dan Java Swing*

</div>
