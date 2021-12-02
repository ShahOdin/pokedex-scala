# Pokedex-Scala

![Screen Shot 2021-12-02 at 02 05 24](https://user-images.githubusercontent.com/13497500/144344171-7bf35e27-d885-4297-87fc-7aa97c786f6e.png)

## Overview

Simple Rest API that proxies calls to two third party apis. Demonstrates the following aspects:

- Json serialisation and deserialization
- Business logic
- Error handling
- Http Server
- Http Client
- Modelling ADTs
- Testing
- code organisation

## Endpoints

The service has two sets of endpoints: 

- Get Pokemon 
  - url: `/pokemon/<name>`
  - example: http://localhost:5000/pokemon/mewtwo
  - example response:
    ```json
    {
      "name": "mewtwo",
      "description": "It was created by a scientist after years of horrific gene splicing and DNA engineering experiments.",
      "habitat": "rare",
      "isLegendary": true
    }
    ```

- Get Pokemon With Translation 
    - url: `/pokemon/translated/<name>`
    - example: http://localhost:5000/pokemon/translated/ditto
    - example response:
      ```json
      {
        "name": "ditto",
        "description": "'t can freely recombine its own cellular structure to transform into other life-forms.",
        "habitat": "urban",
        "isLegendary": false
      }
      ```    


## Running the service

### Local Machine

Assuming you have `sbt` installed locally, from the root directory simply run:

```bash
sbt run
```

### Docker

Assuming you have `docker` installed, from the root directory simply run:

```bash
docker compose up
```

## Next steps

- Some tests are missing
- fix the docker setup

The plan is to write a rust equivalent to compare the two in every aspect.
