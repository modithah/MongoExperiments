CREATE TABLE public."NewExp"
(
    idx bigint NOT NULL DEFAULT nextval('mongoprob_idx_seq'::regclass),
    stname character varying(30) COLLATE pg_catalog."default",
    iter integer,
    run integer,
    col jsonb,
    CONSTRAINT "NewExp" PRIMARY KEY (idx)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public."NewExp"
    OWNER to postgres;