/*
 * COPYRIGHT NOTICE
 * © 2020  Transsmart Holding B.V.
 * All Rights Reserved.
 * All information contained herein is, and remains the
 * property of Transsmart Holding B.V. and its suppliers,
 * if any.
 * The intellectual and technical concepts contained herein
 * are proprietary to Transsmart Holding B.V. and its
 * suppliers and may be covered by European and Foreign
 * Patents, patents in process, and are protected by trade
 * secret or copyright law.
 * Dissemination of this information or reproduction of this
 * material is strictly forbidden unless prior written
 * permission is obtained from Transsmart Holding B.V.
 */
package it.apedano.webservices.restfulwebservices.versioning;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Alessandro Pedano <alessandro.pedano@transsmart.com>
 */
@RestController
public class BesicPersonVersioningController {

    @GetMapping("v1/person")
    public PersonV1 personV1() {
        return new PersonV1("Alessandro");
    }

    @GetMapping("v2/person")
    public PersonV2 personV2() {
        return new PersonV2("Alessandro", "Pedano");
    }

    /*
    curl --location --request GET 'http://localhost:8080/person/param?version=1' \
     */
    @GetMapping(value = "person/param", params = "version=1")
    public PersonV1 personParamV1() {
        return new PersonV1("Alessandro");
    }

    /*
    curl --location --request GET 'http://localhost:8080/person/param?version=2' \
     */
    @GetMapping(value = "person/param", params = "version=2")
    public PersonV2 personParamV2() {
        return new PersonV2("Alessandro", "Pedano");
    }

    /*
    curl --location --request GET 'http://localhost:8080/person/header' \
--header 'X-API-VERSION: 1' \
     */
    @GetMapping(value = "person/header", headers = "X-API-VERSION=1")
    public PersonV1 personHeaderParamV1() {
        return new PersonV1("Alessandro");
    }

    /*
     curl --location --request GET 'http://localhost:8080/person/header' \
--header 'X-API-VERSION: 2' \
     */
    @GetMapping(value = "person/header", headers = "X-API-VERSION=2")
    public PersonV2 personHederParamV2() {
        return new PersonV2("Alessandro", "Pedano");
    }

    /*
    curl --location --request GET 'http://localhost:8080/person/produces' \
--header 'Accept: application/my.company.app-v1+json' \
     */
    @GetMapping(value = "person/produces", produces = "application/my.company.app-v1+json")
    public PersonV1 personProducesV1() {
        return new PersonV1("Alessandro");
    }

    /*
      curl --location --request GET 'http://localhost:8080/person/produces' \
--header 'Accept: application/my.company.app-v2+json' \
     */
    @GetMapping(value = "person/produces", produces = "application/my.company.app-v2+json")
    public PersonV2 personProducesV2() {
        return new PersonV2("Alessandro", "Pedano");
    }

}
