# Lịch Việt (Vạn Niên) - Ứng Dụng Lịch Âm Dương Dân Gian Việt Nam

Ứng dụng **Lịch Việt (Vạn Niên)** được thiết kế và phát triển chuyên sâu cho hệ điều hành Android, hỗ trợ các thiết bị từ **Android 8.0 (API 26) trở lên**. Ứng dụng mang phong cách nghệ thuật dân gian Việt Nam truyền thống, tích hợp thuật toán thiên văn học chuẩn xác tuyệt đối của **TS. Hồ Ngọc Đức (Viện Tin học, Đại học Leipzig)** theo múi giờ Việt Nam (UTC+7).

---

## 📱 Các Màn Hình & Chức Năng Chính (Theo Thiết Kế 5 Ảnh Mẫu)

### 1. Tờ Lịch Ngày (Lịch Ngày - Ảnh 1)
- Hiển thị ngày Dương lịch nổi bật (chữ số to, đậm phong cách dân gian), thứ trong tuần bằng chữ in hoa.
- Hiển thị câu **Ca dao, Tục ngữ, Thành ngữ Việt Nam** đặc sắc (ví dụ: *"Chín bỏ làm mười"*, *"Ăn quả nhớ kẻ trồng cây"*...).
- Khung thông tin Âm lịch 3 phần:
  - **Cột trái**: Đồng hồ kỹ thuật số thời gian thực và Can Chi của giờ hiện tại (kèm biểu tượng Âm Dương Bát Quái).
  - **Cột giữa**: Ngày Âm lịch cỡ lớn và tháng Âm lịch (kèm chỉ báo tháng nhuận).
  - **Cột phải**: Can Chi của Ngày (Ng.), Can Chi của Tháng (Th.), và Can Chi của Năm.
- Cử chỉ chạm vuốt (Swipe Gesture) sang trái/phải để chuyển ngày mượt mà.
- Nút bấm **"Hôm nay"** hình huy hiệu lịch giúp quay về ngày hiện tại tức thì.
- Biểu tượng thời tiết nhanh trên thanh công cụ góc trái dẫn trực tiếp tới màn hình thời tiết.
- Nút vương miện hoàng gia mở nhanh tính năng Tử Vi.

### 2. Đổi Ngày Âm - Dương & Tìm Ngày Tốt (Đổi Ngày - Ảnh 2)
- Gồm 2 tab chuyển đổi: **"Đổi ngày"** và **"Tìm ngày tốt"**.
- Bộ chọn ngày lăn bánh xe (NumberPicker) 2 chiều:
  - Khung **Dương Lịch** (Ngày, Tháng, Năm).
  - Khung **Âm Lịch** (Ngày, Tháng, Năm).
  - Đồng bộ tự động 2 chiều tức thì: chỉnh ngày Dương lịch sẽ cập nhật ngày Âm lịch tương ứng và ngược lại.
- Nút bấm bo tròn **"Xem chi tiết ngày"** mở ngay bảng thông tin chi tiết của ngày đã chọn.
- Chức năng **Tìm ngày tốt**: Lọc các ngày Hoàng Đạo trong tháng theo mục đích (Cưới hỏi, Khai trương, Động thổ, Xuất hành, Vào nhà mới...).

### 3. Tử Vi Toàn Tập (Tử Vi - Ảnh 3)
Lưới 8 mục Tử vi & Phong thủy chuyên sâu:
1. **Tử vi năm**: Xem vận hạn công danh, tài lộc, sức khỏe, tình duyên trong năm.
2. **Tử vi trọn đời**: Luận giải cuộc đời theo năm sinh và giới tính Nam mạng / Nữ mạng.
3. **Xem sao Coi hạn**: Bảng tính sao chiếu mệnh Cửu Diệu (La Hầu, Kế Đô, Thái Bạch, Thái Dương...), hạn Tam Tai, Kim Lâu, Hoang Ốc.
4. **Bói phương đông**: Gieo quẻ Kinh Dịch, lời khuyên Khổng Minh.
5. **Xem ngày tốt - xấu**: Tra cứu Trực, Nhị thập bát tú, giờ cát hung.
6. **Xem tuổi**: Xem tuổi hợp làm ăn, kết hôn, xông đất đầu năm.
7. **Ngày đẹp theo tuổi**: Tra cứu ngày tương sinh với bản mệnh gia chủ.
8. **Phong thủy nhà ở**: Cung mệnh Bát Trạch (4 hướng tốt, 4 hướng xấu) và Thước Lỗ Ban phong thủy.

### 4. Chi Tiết Ngày Dân Gian (Ảnh 4)
- Thanh điều hướng ngày: `< Thứ hai, DD-MM-YYYY >`.
- **Bảng 3 Cột Lịch Âm**: Cột NGÀY (số ngày + Can Chi), Cột THÁNG (tháng + Can Chi), Cột NĂM (năm + Can Chi).
- **Mục Sự kiện**: Các ngày lễ truyền thống dân tộc, lễ Phật giáo, ngày kỷ niệm lịch sử.
- **Mục Giờ Hoàng Đạo**: Danh sách ngang 6 giờ Hoàng Đạo trong ngày kèm biểu tượng 12 con giáp và khung giờ.
- **Mục Hướng Xuất Hành**: Chỉ rõ hướng Hỷ Thần, Tài Thần (hướng tốt nên đi) và Hạc Thần (hướng xấu nên tránh).
- **Mục Sao Tốt - Sao Xấu**: Liệt kê đầy đủ các sao Cát Tinh, Hung Tinh, việc nên làm và việc kiêng cữ trong ngày, Tiết khí và Trực của ngày.

### 5. Dự Báo Thời Tiết Chính Xác (Ảnh 5)
- Hiển thị nhiệt độ lớn trắng nổi bật trên nền trời xanh (ví dụ: `29°`).
- Icon trạng thái thời tiết, mô tả trạng thái (Mây rải rác, Nắng nhẹ, Mưa rào...).
- Chỉ số nhiệt độ cao nhất / thấp nhất (`↓ 30°C / ↑ 25°C`).
- Đo lường: Xác suất có mưa (%), Chỉ số bức xạ UV (Cao/Trung bình), Chất lượng không khí (AQI).
- **Thời tiết 6 giờ tiếp theo**: Danh sách từng giờ (15:00, 16:00, 17:00...) kèm icon, mô tả và nhiệt độ.
- **Dự báo 7 ngày**: Nhiệt độ và thời tiết từng ngày trong tuần.
- Nút chọn vị trí: Hỗ trợ chuyển đổi nhanh giữa 63 tỉnh/thành phố trên khắp cả nước (Hà Nội, TP. Hồ Chí Minh, Đà Nẵng, Hải Phòng, Cần Thơ...).

### 6. Lịch Tháng Dạng Lưới
- Lưới 7 cột (Thứ Hai đến Chủ Nhật).
- Hiển thị song song số ngày Dương và số ngày Âm trên từng ô.
- Đánh dấu chấm màu cho các ngày Hoàng Đạo, ngày Rằm (15) và Mùng 1 đầu tháng.
- Thẻ tóm tắt thông tin ngày được chọn ở đáy màn hình.

### 7. Mở Rộng & Tiện Ích Phong Thủy
- **Thước Lỗ Ban**: Thước 52.2cm (Thông thủy), Thước 42.9cm (Dương trạch), Thước 38.8cm (Âm phần).
- **Văn khấn cổ truyền**: Tuyển tập văn khấn ngày Rằm, Mùng Một, Thần Tài, Lễ Tết.
- **Cài đặt nhắc nhở**: Thông báo tự động ngày Rằm và Mùng 1 hàng tháng.

---

## 🛠 Yêu Cầu Kỹ Thuật & Môi Trường

- **Hệ điều hành Android**: Android 8.0 (API level 26 - Oreo) trở lên.
- **Ngôn ngữ lập trình**: Kotlin (1.9.x / 2.x).
- **Build Tool**: Gradle 8.x, Android Gradle Plugin 8.5+.
- **Kiến trúc**: MVVM (Model - View - ViewModel) với Android Jetpack:
  - `ViewBinding`
  - `ViewModel` & `LiveData`
  - `RecyclerView` & `CardView`
  - `ConstraintLayout`
  - `Material Components 3`

---

## 🚀 Hướng Dẫn Mở Dự Án & Build APK

### Cách 1: Sử dụng Android Studio
1. Mở **Android Studio**.
2. Chọn **Open** và dẫn tới thư mục `/home/ntt/Documents/Lich Viet`.
3. Android Studio sẽ tự động đồng bộ Gradle và tải dependencies.
4. Kết nối điện thoại Android (Android 8.0+) hoặc khởi động máy ảo (Emulator), sau đó bấm **Run** (phím tắt `Shift + F10`) để cài đặt và chạy ứng dụng.

### Cách 2: Build file APK bằng dòng lệnh (Gradle CLI)
Từ thư mục dự án:
```bash
# Cấp quyền thực thi cho gradle wrapper nếu cần
chmod +x gradlew

# Build bản Debug APK:
./gradlew assembleDebug

# File APK sau khi build sẽ nằm tại:
# app/build/outputs/apk/debug/app-debug.apk
```
