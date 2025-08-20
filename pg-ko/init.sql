-- textsearch_ko 확장 활성화
CREATE EXTENSION IF NOT EXISTS textsearch_ko;
ALTER SYSTEM SET default_text_search_config = 'korean';