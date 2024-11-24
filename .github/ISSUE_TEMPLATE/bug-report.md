---
name: Bug report
about: 'Bug report template '
title: "[Bug Report] Bug title"
labels: bug
assignees: 'Hieu'

---

Chức năng xảy ra vấn đề: 

 - **Mô tả:** 
 - **Nếu hoạt động đúng:** 
 -  **Các bước tạo ra lỗi:**
1. Thiếu kiểm tra các định dạng INT gây ra lỗi ( năm,id,số lượng)
   Integer.parseInt và scanner.nextInt ở các mục main, updateDocument, findDocumentByID, removeDocument, addDocument
2. Khi mượn và trả sách Quantity không thay đổi ( Thêm code kiểm tra nếu hết sách thì không cho mượn nếu chưa có)
   Danh sách khi người dùng mượn sách không được lưu
3. Không nhập được ngày tháng phải trả sách do dòng 161 không được thực thi
4. ID người mượn không được kiểm tra có tồn tại không khi mượn sách
5. Lỗi logic ở phần id sách
   Làm cho 2 quyển sách có cùng 1 id, id khi in ra ở menu 5 cũng bị lỗi thứ tự
6. Lỗi logic khi nhập người dùng mới. 
   (User DinhHieu created successfully with id 3) Mặc dù id được lưu trong danh sách người dùng là 2
7. Nhiều khi menu bị in ra 1 lần trường hợp default menu sau khi thao tác
   Thiếu break ở case 11 menu
8. Thiếu kiểm tra null id ở phần mượn sách và phần showUserBorrowedDocuments
9. Kiểm tra trường hợp nếu không có sách trong thư viện
10. Thiếu kiểm tra ở menu 8 nếu người dùng chưa mượn sách để break luôn
   Choose: 8
   ====Return document=====
   Enter user ID who's returning the document: 1 ( user 1 đã mượn sách)
   User borrowed documents: (không có sách được mượn)
   ====Show user borrowed documents=====
   Enter user ID: 1 ( Show user borrowed documents được tự động gọi ra làm chương trình hỏi id người dùng 2 lần)
   User TranDinhHieu has not borrowed any document yet.
   Enter document ID to return: 1 ( mặc dù không có sách mượn nhưng vẫn bắt trả sách)
   No borrow record found for user 1 and document 1
**Video/Ảnh chụp commandline và lỗi được hiện thị trong commandline:**

[Chèn video tạo lỗi hoặc ảnh chụp phần lỗi vào đây (kéo dài commandline để thể hiện các bước trước khi xảy ra lỗi)]

[Copy lỗi được ghi hoặc phần chữ đỏ khi bị crash vào đây]
