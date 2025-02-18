-- Generated source code can be different from the original.

--------------------------------------------------------------------------------
-- Table : public.contact_fields
create table public.contact_fields (
  id bigserial not null
  , address character varying(50)
  , company_name character varying(50)
  , date_added timestamp(6) without time zone
  , first_name character varying(50)
  , last_name character varying(50)
  , phone_number character varying(11)
  , postal_code character varying(7)
  , primary key (id)
);
/


--------------------------------------------------------------------------------
-- Table : public.role_properties
create table public.role_properties (
  role character varying(20) not null
  , role_id serial not null
  , primary key (role_id)
);
/

create unique index unique_role
  on public.role_properties(role)
/


--------------------------------------------------------------------------------
-- Table : public.user_basics
create table public.user_basics (
  id serial not null
  , username character varying(50) not null
  , email character varying(50) not null
  , primary key (id)
);
/

create unique index unique_email1
  on public.user_basics(email)
/

create unique index unique_username1
  on public.user_basics(username)
/


--------------------------------------------------------------------------------
-- Table : public.user_history
create table public.user_history (
  id serial not null
  , username character varying(50) not null
  , date_added timestamp(6) without time zone not null
  , last_login timestamp(6) without time zone not null
  , primary key (id)
);
/


--------------------------------------------------------------------------------
-- Table : public.user_properties
create table public.user_properties (
  id serial not null
  , username character varying(50) not null
  , active boolean default true not null
  , date_added timestamp(6) without time zone not null
  , email character varying(50) not null
  , role_id integer not null
  , verification_token character varying(50) not null
  , token_date timestamp(6) without time zone
  , primary key (id)
);
/

create unique index unique_email
  on public.user_properties(email)
/

create unique index unique_username
  on public.user_properties(username)
/


--------------------------------------------------------------------------------
-- Table : public.user_security
create table public.user_security (
  id serial not null
  , username character varying(50) not null
  , password character varying(255) not null
  , primary key (id)
);
/

create unique index userunique
  on public.user_security(username)
/


