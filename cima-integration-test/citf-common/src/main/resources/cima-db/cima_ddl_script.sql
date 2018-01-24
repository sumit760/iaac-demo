SET DATABASE DEFAULT TABLE TYPE MEMORY;
SET DATABASE SQL SYNTAX MYS TRUE;

CREATE TABLE IF NOT EXISTS users (
  user_id varchar(25) NOT NULL,
  password blob NOT NULL,
  category varchar(15) NOT NULL,
  pin varchar(5) DEFAULT NULL,
  tv_rating varchar(50) DEFAULT NULL,
  movie_rating varchar(50) DEFAULT NULL,
  environment varchar(10) NOT NULL,
  login_status varchar(15) NOT NULL DEFAULT 'ACTIVE',
  PRIMARY KEY (user_id)
);


CREATE TABLE IF NOT EXISTS user_attributes (
  guid varchar(100) NOT NULL,
  user_id varchar(25) NOT NULL,
  email varchar(150) DEFAULT NULL,
  alternate_email varchar(150) DEFAULT NULL,
  alternate_email_password varchar(20) DEFAULT NULL,
  secret_question varchar(500) DEFAULT NULL,
  secret_answer varchar(200) DEFAULT NULL,
  facebook_id varchar(100) DEFAULT NULL,
  dob date DEFAULT NULL,
  ssn varchar(15) DEFAULT NULL,
  facebook_password varchar(20) DEFAULT NULL,
  PRIMARY KEY (guid),
  CONSTRAINT users_user_attr_f_key FOREIGN KEY (user_id) REFERENCES users (user_id)
) ;


CREATE TABLE IF NOT EXISTS fresh_ssn_info (
  ssn varchar(15) NOT NULL,
  dob date NOT NULL,
  ssn_creation_date date NOT NULL,
  PRIMARY KEY (ssn)
) ;


CREATE TABLE IF NOT EXISTS accounts (
  billing_account_id varchar(25) NOT NULL,
  environment varchar(10) NOT NULL,
  billing_system varchar(10) DEFAULT NULL,
  auth_guid varchar(100) NOT NULL,
  account_status varchar(15) NOT NULL DEFAULT 'ACTIVE',
  first_name varchar(100) DEFAULT NULL,
  last_name varchar(100) DEFAULT NULL,
  phone varchar(15) DEFAULT NULL,
  address varchar(500) DEFAULT NULL,
  zip_code varchar(10) DEFAULT NULL,
  transfer_flag varchar(1) DEFAULT NULL,
  physical_resource_link varchar(100) DEFAULT NULL,
  fresh_ssn varchar(15) DEFAULT NULL,
  PRIMARY KEY (billing_account_id),
  CONSTRAINT acc_fresh_ssn_f_key FOREIGN KEY (fresh_ssn) REFERENCES fresh_ssn_info (ssn)
) ;

CREATE TABLE IF NOT EXISTS channels (
  channel_id varchar(50) NOT NULL,
  name varchar(100) NOT NULL,
  description varchar(500) DEFAULT NULL,
  station_id varchar(50) NOT NULL,
  content_type varchar(10) DEFAULT 'TV,MOVIE',
  test_strategy varchar(25) DEFAULT NULL,
  status varchar(10) NOT NULL DEFAULT 'ACTIVE',
  PRIMARY KEY (channel_id)
) ;


CREATE TABLE IF NOT EXISTS asset_rating (
  rating_name varchar(25) NOT NULL,
  rating_type varchar(10) NOT NULL,
  priority int NOT NULL,
  PRIMARY KEY (rating_name,rating_type)
) ;

CREATE TABLE IF NOT EXISTS assets (
  name varchar(250) NOT NULL,
  channel_id varchar(50) DEFAULT NULL,
  tv_rating varchar(25) DEFAULT NULL,
  movie_rating varchar(25) DEFAULT NULL,
  series_name varchar(200) DEFAULT NULL,
  series_id varchar(20) DEFAULT NULL,
  program_id varchar(25) DEFAULT NULL,
  status varchar(10) DEFAULT 'ACTIVE',
  PRIMARY KEY (name),
  CONSTRAINT ast_channel_id_f_key FOREIGN KEY (channel_id) REFERENCES channels (channel_id)
) ;


CREATE TABLE IF NOT EXISTS channel_user_map (
  channel_id varchar(20) NOT NULL DEFAULT '0',
  user_id varchar(25) NOT NULL DEFAULT '',
  subscription_value varchar(100) NOT NULL,
  PRIMARY KEY (channel_id,user_id),
  CONSTRAINT cumap_channelid_f_key FOREIGN KEY (channel_id) REFERENCES channels (channel_id),
  CONSTRAINT cumap_userid_f_key FOREIGN KEY (user_id) REFERENCES users (user_id)
) ;


CREATE TABLE IF NOT EXISTS user_account_map (
  user_id varchar(25) NOT NULL,
  account_id varchar(25) NOT NULL,
  user_role varchar(1) NOT NULL DEFAULT 'P',
  PRIMARY KEY (user_id,account_id),
  CONSTRAINT uamap_ac_id_f_key FOREIGN KEY (account_id) REFERENCES accounts (billing_account_id),
  CONSTRAINT uamap_user_id_f_key FOREIGN KEY (user_id) REFERENCES users (user_id)
) ;


CREATE TABLE IF NOT EXISTS fresh_users (
  sys_id int NOT NULL AUTO_INCREMENT,
  alternate_email varchar(150) DEFAULT NULL,
  alternate_email_password varchar(20) DEFAULT NULL,
  facebook_id varchar(100) DEFAULT NULL,
  facebook_password varchar(20) DEFAULT NULL,
  last_generated_user_id varchar(25) DEFAULT NULL,
  last_modified_on timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  lock_status varchar(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (sys_id)
) ;


CREATE TABLE IF NOT EXISTS temp_parameters (
  param_key varchar(50) NOT NULL,
  param_value text,
  additional_val1 varchar(200) DEFAULT NULL,
  additional_val2 varchar(500) DEFAULT NULL,
  last_modified_on timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  last_modified_by varchar(20) DEFAULT NULL,
  status varchar(10) NOT NULL DEFAULT 'ACTIVE',
  PRIMARY KEY (param_key)
) ;

CREATE TABLE IF NOT EXISTS browser_capabilities (
  cap_key varchar(100) NOT NULL,
  type varchar(15) NOT NULL,
  platform varchar(20) NOT NULL,
  platform_version double DEFAULT NULL,
  browser varchar(20) DEFAULT NULL,
  browser_version double DEFAULT NULL,
  device varchar(100) DEFAULT NULL,
  device_orientattion varchar(20) DEFAULT NULL,
  status varchar(15) NOT NULL DEFAULT 'ACTIVE',
  PRIMARY KEY (cap_key)
) ;

CREATE TABLE IF NOT EXISTS servers (
  server_type varchar(20) NOT NULL,
  environment varchar(10) NOT NULL,
  priority int NOT NULL,
  host varchar(100) NOT NULL,
  port varchar(4) NOT NULL,
  user_id varchar(50) NOT NULL,
  password varchar(20) DEFAULT NULL,
  status varchar(10) NOT NULL DEFAULT 'ACTIVE',
  PRIMARY KEY (server_type, environment, priority)
) ;

CREATE TABLE IF NOT EXISTS log_finders (
  id varchar(50) NOT NULL,
  environment varchar(10) NOT NULL,
  log_path varchar(200) NOT NULL,
  regex varchar(150) NOT NULL,
  splunk_query varchar(500) NOT NULL,
  splunk_query_key varchar(50) NOT NULL,
  PRIMARY KEY (id, environment)
) ;
