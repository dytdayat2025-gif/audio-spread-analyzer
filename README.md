# Audio Spread Analyzer

Aplikasi Android untuk mengukur distribusi suara dari speaker atau sound system dengan memanfaatkan mikrofon handphone sebagai sensor penerima.

Fitur utama:
- Pengukuran level suara real-time dari mikrofon handphone
- Input lokasi/posisi pengukuran
- Tampilan level saat ini, rata-rata, maksimum, dan minimum
- Riwayat pengukuran per titik lokasi
- Ekspor data ke CSV
- Antarmuka berbasis Jetpack Compose

Catatan penting:
- Nilai yang ditampilkan bersifat relatif dari mikrofon handphone, bukan pengukuran SPL yang terkalibrasi sepenuhnya.
- Hasil bisa berbeda antar perangkat, dan untuk akurasi tinggi disarankan membandingkan dengan sound level meter referensi.

Struktur proyek:
- app/src/main/java/com/audiospreadanalyzer/app/ui : komponen UI aplikasi
- app/src/main/java/com/audiospreadanalyzer/app/domain/usecase : use case pengukuran
- app/src/main/java/com/audiospreadanalyzer/app/data : model dan repository data
- app/src/main/java/com/audiospreadanalyzer/app/util : utilitas audio

Cara menjalankan:
1. Buka proyek di Android Studio
2. Pastikan Android SDK terinstall
3. Sambungkan handphone atau jalankan emulator
4. Jalankan aplikasi
5. Berikan izin akses mikrofon
6. Letakkan handphone di titik pengukuran, lalu mulai pengukuran

Metode pengujian:
- Tetapkan titik pengukuran di depan speaker, kiri, kanan, tengah, dan sudut ruangan
- Catat hasil di setiap titik
- Bandingkan level suara antar titik untuk melihat distribusi audio

Skema interpretasi:
- Level tinggi di pusat ruangan = distribusi suara cukup baik
- Penurunan tajam di sudut = area yang kurang tercover
- Selisih signifikan kiri-kanan = imbalance output speaker

Lisensi:
Proyek ini dibuat untuk kebutuhan eksperimen, pengukuran suara sederhana, dan evaluasi distribusi audio secara praktis.
