<h1 align="center">Gift Certificate</h1>
<h4 align="center">Rest application for working with gift certificates, tags, users, orders</h4>

      Java '17'    |    Gradle '7.4.2'     |    Docker '20.10.23'    |    PostgreSQL     |    SpringBoot '3.0.6'

---

<h3 align="center">Example of using a gift certificate (other entities work in a similar way)</h3>

- #### Get all:
    - ###### Request `http://localhost:8080/gcs`

    - ###### Response
        <pre>[
        {
            "id": 1,
            "name": "n1",
            "description": "d1",
            "price": 1.1,
            "duration": 1,
            "createDate": "2023-01-01T01:11:11:111",
            "lastUpdateDate": "2023-01-01T01:11:11:111",
            "tags": [
                {
                    "id": 1,
                    "name": "n1"
                }
            ]
        },
        {
            "id": 3,
            "name": "n3",
            "description": "d3",
            "price": 3.3,
            "duration": 3,
            "createDate": "2023-03-03T03:33:33:333",
            "lastUpdateDate": "2023-03-03T03:33:33:333",
            "tags": [
                {
                    "id": 4,
                    "name": "n4"
                },
                {
                    "id": 5,
                    "name": "n5"
                },
                {
                    "id": 6,
                    "name": "n6"
                }
            ]
        },
        ...
        ]</pre>

- #### Get by id:
    - ###### Request`http://localhost:8080/gcs/3`

    - ###### Response
        <pre>{
        "id": 3,
            "name": "n3",
            "description": "d3",
            "price": 3.3,
            "duration": 3,
            "createDate": "2023-03-03T03:33:33:333",
            "lastUpdateDate": "2023-03-03T03:33:33:333",
            "tags": [
        {
            "id": 4,
                "name": "n4"
        },
        {
            "id": 5,
                "name": "n5"
        },
        {
            "id": 6,
                "name": "n6"
        }
      }</pre> 

- #### Post:
    - ###### Request body:
        <pre>{
        "name": "ert",
            "description": "dis",
            "price": 3.3,
            "duration": 3,
            "tags": [
            {
            "name": "newNameTag"
            }
            ]
        }</pre> 
    - ###### Response
        <pre>{
        "id": 12,
            "name": "ert",
            "description": "dis",
            "price": 3.3,
            "duration": 3,
            "createDate": "2023-05-12T00:42:01:731",
            "lastUpdateDate": "2023-05-12T00:42:01:731",
            "tags": [
        {
            "id": 83,
                "name": "newNameTag"
        },
        {
            "id": 84,
                "name": "ert"
        }
        ]
      }</pre>

- #### Put:
    - ###### Request body:
        <pre>{
        "id": 12,
            "name": "ert2",
            "description": "dis2",
            "price": 4.4,
            "duration": 4,
            "tags": [
          {
            "name": "ert"
          }
          ]
        }</pre> 
    - ###### Response
        <pre>{
        "id": 12,
            "name": "ert2",
            "description": "dis2",
            "price": 4.4,
            "duration": 4,
            "createDate": "2023-05-12T00:42:01:731",
            "lastUpdateDate": "2023-05-12T00:44:03:813",
            "tags": [
        {
            "id": 84,
                "name": "ert"
        },
        {
            "id": 85,
                "name": "ert2"
        }
        ]
      }</pre>

- #### Patch:
    - ###### Request `http://localhost:8080/gcs/12`
    - ###### Body:
      <pre>{
      "field": "price",
          "value": 99
      }</pre> 
    - ###### Response
        <pre>{
        "id": 12,
            "name": "ert2",
            "description": "dis2",
            "price": 99.0,
            "duration": 4,
            "createDate": "2023-05-12T00:42:01:731",
            "lastUpdateDate": "2023-05-12T00:47:10:226",
            "tags": [
        {
            "id": 84,
                "name": "ert"
        },
        {
            "id": 85,
                "name": "ert2"
        }
        ]
      }</pre>

- #### Delete:
    - ###### Request `http://localhost:8080/gcs/12`
    - ###### Response
        <pre>204 No Content</pre>