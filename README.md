# Audio Spread Analyzer

Aplikasi Android untuk mengukur distribusi suara/suara yang keluar dari speaker atau sound system dengan memanfaatkan mikrofon handphone sebagai sensor penerima.

Fitur utama:
- Pengukuran level suara real-time dari mikrofon handphone
- Input lokasi/posisi pengukuran
- Pengukuran level saat ini, rata-rata, maksimal, dan minimal
- Riwayat pengukuran per titik lokasi
- Ekspor data pengukuran ke file CSV
- Antarmuka berbasis Jetpack Compose

Catatan penting:
- Nilai yang ditampilkan adalah level relatif dari mikrofon handphone, bukan hasil kalibrasi SPL profesional.
- Untuk penggunaan akurasi tinggi, disarankan membandingkan hasil dengan sound level meter referensi.

Struktur proyek:
- `app/src/main/java/com/audiospreadanalyzer/app/ui` : komponen UI aplikasi
- `app/src/main/java/com/audiospreadanalyzer/app/domain/usecase` : use case pengukuran
- `app/src/main/java/com/audiospreadanalyzer/app/data` : model dan repository data
- `app/src/main/java/com/audiospreadanalyzer/app/util` : utilitas audio

Cara menjalankan:
1. Buka proyek di Android Studio
2. Pastikan SDK Android tersedia
3. Hubungkan handphone atau emulator
4. Jalankan aplikasi
5. Berikan izin akses mikrofon
6. Letakkan handphone di titik pengukuran, lalu mulai pengukuran

Skema pengukuran:
- Gunakan titik-titik pengukuran yang sudah ditentukan di ruangan
- Catat posisi seperti depan speaker, kiri speaker, kanan speaker, tengah ruangan, sudut ruangan
- Bandingkan level suara antar titik untuk memetakan sebaran suara

Contoh analisis:
- Lebih tinggi di pusat ruangan = distribusi suara merata
- Penurunan tajam di sudut = area low coverage
- Perbedaan besar antara kiri dan kanan = imbalance channel / balancing audio

Persiapan pengujian:
- Gunakan sound system dengan volume konstan
- Hindari noise background saat pengukuran
- Ulangi pengukuran beberapa kali untuk konsistensi data

Lisensi:
Proyek ini dibuat untuk kebutuhan eksperimen dan pengukuran suara sederhana.
