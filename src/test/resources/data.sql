INSERT INTO place(id, place_id, subject, content, create_dt, create_by, delete_yn)
VALUES (101, '0cc30f8b-d619-44a1-9afa-197fec075792', 'subject1', 'content1', now(), 'none', false),
       (102, '1cc30f8b-d619-44a1-9afa-197fec890531', 'subject2', 'content2', now(), 'none', false),
       (103, '2cc30f8b-d619-44a1-9afa-197fec789343', 'subject3', 'content3', now(), 'none', false),
       (104, '3cc30f8b-d619-44a1-9afa-197fec789343', 'subject4', 'content4', now(), 'none', false);

INSERT INTO users(id, user_id, name, email, password, create_dt, create_by, delete_yn)
VALUES (2, 'UT3ee7379d-955b-4e15-821c-a096ef141ed1', '유저 이름', 'user@inter.com',
       '$2a$10$wcOwNqVSOBzS4DtnWwfkR./H8XRRQom34aOAqRrtgoaHJ7pyWOzvS', now(), 'none', false),
       (3, 'UT2qweqw-asdqw12-qwd1q-qweasd', '유저 이름2', 'user2@inter.com',
      '$2a$10$wcOwNqVSOBzS4DtnWwfkR./H8XRRQom34aOAqRrtgoaHJ7pyWOzvS', now(), 'none', false);

INSERT INTO point(id, point_id, user_id, point, create_dt, create_by, delete_yn)
VALUES (2, '15waz23d-955b-4e15-821c-q90re0qaz90', 2, 0, now(), 'none', false),
       (3, '12qwe12a-955b-4e15-821c-q90re0qaz90', 3, 0, now(), 'none', false);

INSERT INTO review(id, review_id, subject, content, star, user_id, place_id, create_dt, create_by, delete_yn)
VALUES (3, 'sfd980ew-d619-44a1-9afa-197fec789343', '리뷰 제목1', '리뷰 내용1', 3.2, 2, 101, now(), 'none', false),
       (4, 'asd167as-d619-44a1-9afa-197fec789343', '리뷰 제목2', '리뷰 내용2', 3.2, 3, 102, now(), 'none', false);

INSERT INTO review_image(id, review_image_id, name, review_id, create_dt, create_by, delete_yn)
VALUES  (2, '5qwe892d-955b-4e15-821c-a096ef141ed1', '리뷰 사진', 4, now(), 'none', false);