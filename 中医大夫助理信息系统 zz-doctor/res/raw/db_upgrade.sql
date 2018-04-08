/*** No upgrade before 1.0 ***
alter table RX_RECIPE
    add column SYNDROME_ID  integer     references SYNDROME_SUBJECT(PK_ID) on delete restrict;
*/
