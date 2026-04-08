# Movie Ticket Firebase App (Android Java)

Ứng dụng Android Java mẫu cho bài toán đặt vé xem phim với Firebase.

## Chức năng
- Đăng ký / đăng nhập bằng Firebase Authentication
- Xem danh sách phim từ Cloud Firestore
- Xem suất chiếu
- Đặt vé
- Lưu vé vào Firestore
- Gửi và nhận push notification qua FCM
- Lưu poster phim bằng Firebase Storage (đã để sẵn chỗ dùng URL)

## Công nghệ
- Android Java
- Firebase Authentication
- Cloud Firestore
- Firebase Cloud Messaging
- Firebase Storage
- Material Components
- RecyclerView

## Cấu trúc Firestore đề xuất

### collection: users
- uid
- name
- email
- phone
- createdAt

### collection: movies
- title
- description
- duration
- genre
- posterUrl
- releaseDate

### collection: theaters
- name
- address

### collection: showtimes
- movieId
- theaterId
- room
- startTimeText
- timestamp
- price
- bookedSeats (array string)

### collection: tickets
- userId
- movieId
- movieTitle
- theaterId
- theaterName
- showtimeId
- showtimeText
- seatNumber
- price
- status
- bookingTime

## Cách chạy

1. Tạo Firebase project trên Firebase Console.
2. Thêm app Android với package:
   `com.example.movieticketfirebase`
3. Tải file `google-services.json` và chép vào:
   `app/google-services.json`
4. Bật:
   - Authentication / Email-Password
   - Cloud Firestore
   - Cloud Messaging
   - Storage
5. Chạy app bằng Android Studio.

## Dữ liệu mẫu Firestore

Tạo `movies`:
```json
{
  "title": "Avengers: Endgame",
  "description": "Phim siêu anh hùng Marvel",
  "duration": 180,
  "genre": "Action",
  "posterUrl": "",
  "releaseDate": "2026-04-08"
}
```

Tạo `theaters`:
```json
{
  "name": "CGV Vincom",
  "address": "Quan 1, TP.HCM"
}
```

Tạo `showtimes`:
```json
{
  "movieId": "ID_MOVIE",
  "theaterId": "ID_THEATER",
  "room": "Room 1",
  "startTimeText": "2026-04-10 19:30",
  "timestamp": 1775824200000,
  "price": 90000,
  "bookedSeats": []
}
```

## Firestore Rules gợi ý

```js
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {

    match /movies/{doc} {
      allow read: if true;
    }

    match /theaters/{doc} {
      allow read: if true;
    }

    match /showtimes/{doc} {
      allow read: if true;
    }

    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }

    match /tickets/{ticketId} {
      allow create, read: if request.auth != null;
    }
  }
}
```

## Ghi chú quan trọng
- Mình không thể đính kèm `google-services.json` thật vì file này phải lấy từ Firebase project của bạn.
- Thông báo nhắc giờ chiếu có thể gửi bằng:
  - FCM từ Firebase Console
  - hoặc Cloud Functions / server riêng nếu muốn tự động trước giờ chiếu.
- App này là mẫu hoàn chỉnh đủ để mở trong Android Studio, build tiếp, và tùy biến giao diện.
