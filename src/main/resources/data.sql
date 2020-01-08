/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Alessandro
 * Created: Jan 7, 2020
 */

INSERT INTO user (id, birth_date, name) VALUES (10001, '1980-07-27 00:00:00', 'Alessandro');
INSERT INTO user (id, birth_date, name) VALUES (10002, '1989-04-26 00:00:00', 'Silvia');
INSERT INTO user (id, birth_date, name) VALUES (10003, sysdate(), 'Jill');

INSERT INTO post VALUES(11001, 'My fisrt post', 10001);
INSERT INTO post VALUES(11002, 'My second post', 10001);