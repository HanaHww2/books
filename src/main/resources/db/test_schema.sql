ALTER SYSTEM SET default_text_search_config = 'pg_catalog.simple';
SELECT pg_reload_conf();

-- BOOK
ALTER TABLE books ALTER COLUMN id SET DEFAULT nextval('books_seq');
SELECT setval('books_seq', 101, false);

-- `books` 테이블에 `tsv` 컬럼 추가 (검색을 위한 컬럼)
ALTER TABLE books
    ADD COLUMN IF NOT EXISTS tsv tsvector
    GENERATED ALWAYS AS (
    to_tsvector('simple', coalesce(title,'') || ' ' || coalesce(sub_title,''))
    ) STORED;

-- 인덱스
CREATE INDEX tsv_books_idx ON books USING GIN (tsv);