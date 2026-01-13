# TimeBlackBox - Aplikasi Produktivitas & Focus Timer

**Laporan Tugas Besar Mata Kuliah Pemrograman Berorientasi Objek (PBO)**

Aplikasi ini dirancang untuk membantu pengguna meningkatkan produktivitas dengan mencatat waktu fokus belajar atau bekerja. Aplikasi menerapkan konsep gamifikasi sederhana di mana pengguna mendapatkan poin berdasarkan durasi fokus mereka, dengan target harian tertentu.

## 👤 Identitas Mahasiswa

* **Nama:** Muhammad Bagas Maulana
* **NIM:** 32602400025
* **Kelas:** A
* **Mata Kuliah:** Pemrograman Berorientasi Objek
* **Dosen Pengampu:** Sam Farisa Chaerul Haviana, ST., M.Kom

---

## 🛠️ Fitur Utama

1.  **User Authentication:** Sistem Login dan Register dengan validasi username unik.
2.  **Focus Timer:** Timer waktu nyata untuk mencatat sesi produktivitas.
3.  **Gamification:** Sistem poin otomatis (Target 4 jam = 100 poin).
4.  **Auto-Categorization:** Pengkategorian otomatis berdasarkan durasi (Terdistraksi, Cukup Fokus, Sangat Fokus, dll).
5.  **Activity History:** Riwayat aktivitas harian tersimpan dalam database.
6.  **Data Persistence:** Menggunakan SQLite Database yang portable.
7.  **Auto-Save:** Penyimpanan otomatis saat aplikasi ditutup paksa ketika timer berjalan.

---

## 📚 Penerapan Konsep OOP

Aplikasi ini dibangun dengan menerapkan 4 pilar utama Pemrograman Berorientasi Objek:

### 1. Encapsulation (Pembungkusan)
Membungkus data (variabel) agar tidak bisa diakses langsung dari luar class, melainkan melalui method akses (getter/setter).
* **Implementasi:** Class `model/User.java` menggunakan modifier `private` pada field `username` dan `password`, serta menyediakan public getter/setter.
* **Lokasi:** `model/User.java`, `util/DatabaseConnection.java` (Konstanta DB_URL bersifat private).

### 2. Inheritance (Pewarisan)
Membuat class baru berdasarkan class yang sudah ada untuk menggunakan kembali kode umum.
* **Implementasi:** `FocusSession` dan `BreakSession` mewarisi atribut (`name`, `duration`) dari parent class `TimeBlock`.
* **Lokasi:** `model/FocusSession.java extends TimeBlock`, `model/BreakSession.java extends TimeBlock`.

### 3. Polymorphism (Banyak Bentuk)
Kemampuan objek untuk memiliki banyak bentuk. Satu method yang sama memiliki implementasi berbeda pada class anak (Overriding).
* **Implementasi:** Method `calculateProductivityScore()` pada parent `TimeBlock` di-override. 
    * Di `FocusSession`: Menghitung poin berdasarkan durasi.
    * Di `BreakSession`: Mengembalikan nilai 0 (istirahat tidak menambah poin).
* **Lokasi:** `model/FocusSession.java`, `model/BreakSession.java`, dan penggunaan `List<TimeBlock>` di `service/TimeAnalyzer.java`.

### 4. Abstraction (Abstraksi)
Menyembunyikan detail implementasi yang kompleks dan hanya menampilkan fungsionalitas penting.
* **Implementasi:** `TimeBlock` dibuat sebagai **Abstract Class** karena tidak boleh dibuat objeknya secara langsung, hanya turunannya yang boleh. Selain itu, penggunaan Interface `ActionListener` pada GUI.
* **Lokasi:** `model/TimeBlock.java` (abstract), `dao/TimeBlockDAO.java` (menyembunyikan query SQL dari UI).

---

## 💻 Cara Menjalankan Aplikasi (Panduan untuk Dosen/Pengguna Lain)

Aplikasi ini menggunakan **SQLite** sehingga tidak memerlukan instalasi server database (MySQL/PostgreSQL). Database akan dibuat otomatis dalam folder proyek.

### Persyaratan Sistem
1.  JDK (Java Development Kit) minimal versi 8.
2.  NetBeans IDE / IntelliJ IDEA / Eclipse.
3.  **Library SQLite JDBC Driver** (Sangat Penting).

### Langkah-langkah Instalasi
1.  **Download/Clone** repository ini.
2.  Buka proyek di IDE (misal: NetBeans).
3.  **Tambahkan Library:**
    * Klik kanan pada folder `Libraries` di proyek.
    * Pilih *Add JAR/Folder*.
    * Masukkan file `sqlite-jdbc-x.x.x.jar`. (File driver biasanya disertakan dalam folder `lib` atau harus diunduh terpisah).
4.  **Jalankan Aplikasi:**
    * Cari file `ui/LoginFrame.java`.
    * Klik Kanan -> **Run File** (Shift + F6).
5.  **Database:**
    * File `timeblackbox.db` akan otomatis dibuat di root folder proyek saat aplikasi pertama kali dijalankan.

---

## ⚙️ Catatan Pengujian (Developer Mode)

Secara default, aplikasi menghitung waktu secara **Real-Time** (1 menit = 60 detik). Untuk mempercepat pengujian/presentasi agar tidak perlu menunggu lama, Anda dapat mengubah logika konversi waktu.

**Lokasi File:** `src/ui/MainFrame.java`

Cari method `stopTimerAndSave()` dan lakukan perubahan berikut:

```java
private void stopTimerAndSave() {
    if (!isRunning) return;

    // Hentikan Timer
    timer.stop();
    isRunning = false;
    long endTimeMillis = System.currentTimeMillis();
    
    // Hitung durasi
    long durationMillis = endTimeMillis - startTimeMillis;

    // --- MODE: REAL TIME (DEFAULT) ---
    // Gunakan baris ini untuk penggunaan normal:
    int durationMinutes = (int) (durationMillis / (1000 * 60)); 
    
    // --- MODE: TESTING / PRESENTASI ---
    // Gunakan baris ini untuk demo (1 Detik dianggap 1 Menit):
    // int durationMinutes = (int) (durationMillis / 1000); 

    // ... sisa kode ...
}
