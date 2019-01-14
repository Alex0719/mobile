var express = require('express');
var router = express.Router();
const pg = require('pg');
const path = require('path');
const connectionString = process.env.DATABASE_URL || 'postgres://postgres:latituded830@localhost:5432/mobile-app';
const jwt  = require('jsonwebtoken');
const WebSocketServer = require('ws').Server;
const wss = new WebSocketServer({port: 4000});

let tok = '';
const broadcast = data => {
    wss.clients.forEach((conn) => {
      console.log('trimit date');
      conn.send(JSON.stringify(data));
    });
}


/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

router.post('/api/masini', (req, res, next) => {
  const results = [];
  // Grab data from http request
  const data = {marca: req.body.marca, combustibil: req.body.combustibil};
  // Get a Postgres client from the connection pool
  pg.connect(connectionString, (err, client, done) => {
    // Handle connection errors
    if(err) {
      done();
      console.log(err);
      return res.status(500).json({success: false, data: err});
    }
    // SQL Query > Insert Data
    client.query('INSERT INTO masini(marca, combustibil) values($1, $2)',
    [data.marca, data.combustibil]);
    // SQL Query > Select Data
    const query = client.query('SELECT * FROM masini ORDER BY id ASC');
    // Stream results back one row at a time
    query.on('row', (row) => {
      results.push(row);
    });

    // After all data is returned, close connection and return results
    query.on('end', () => {
      done();
      broadcast({ masini: results });
      return res.json(results);
    });
  });
});

//UPDATE masina
router.put('/api/masini', (req, res, next) => {
  const masini = [];

  const data = {id: req.body.id, marca: req.body.marca, combustibil: req.body.combustibil};

  pg.connect(connectionString, (err, client, done) => {
    if(err) {
      done();
      console.log(err);
      return res.status(500).json({success: false, data: err});
    }

    client.query('UPDATE masini SET marca = $1, combustibil = $2 WHERE id=$3',
    [data.marca, data.combustibil, data.id]);

    const query = client.query('SELECT * FROM masini ORDER BY id ASC');

    query.on('row', (row) => {
      masini.push(row);
    });

    query.on('end', () => {
      done();
      broadcast({ masini });
      return res.json({ masini });
    });
  });
});

router.delete('/api/masini', (req, res, next) => {
  const masini = [];

  const data = {id: req.body.id};

  pg.connect(connectionString, (err, client, done) => {
    if(err) {
      done();
      console.log(err);
      return res.status(500).json({success: false, data: err});
    }

    client.query('DELETE FROM masini WHERE id=$1',
    [data.id]);

    const query = client.query('SELECT * FROM masini ORDER BY id ASC');

    query.on('row', row => {
      masini.push(row);
    });

    query.on('end', () => {
      done();
      broadcast({ masini });
      return res.json({ masini });
    });
  });
});

router.put('/synchronize', (req, res, next) => {
  const data = req.body.masini;
  pg.connect(connectionString, (err, client, done) => {
    if(err) {
      done();
      console.log(err);
      return res.status(500).json({success: false, data: err});
    }
    console.log('offline data', data);
    data.forEach(masina => {
      client.query('UPDATE masini SET marca=$1, combustibil=$2 WHERE id=$3',
      [masina.marca, masina.combustibil, masina.id]);
    });
    broadcast({masini: data});
  });
});

router.post('/api/login',  (req, res, next) => {
  const results = [];

  const data = {nume: req.body.nume, parola: req.body.parola};

  pg.connect(connectionString, (err, client, done) => {
    if(err) {
      done();
      console.log(err);
      return res.status(500).json({success: false, data: err});
    }

    const query = client.query('SELECT * from utilizatori WHERE nume=$1 AND parola=$2',
    [data.nume, data.parola]);

    query.on('row', (row) => {
      results.push(row);
    });

    query.on('end', () => {
      done();
      if(results.length > 0) {
        const token = jwt.sign({nume: data.nume}, 'key');
        tok = token;
        return res.json({found: true, token: token});
      } else {
        return res.json({found: false});
      }
    });
  });
});

router.get('/api/masini', (req, res, next) => {
  const masini = [];
  console.log('headers', req.headers.authorization.split(' ')[1]);
  console.log('tok', tok);
  // res.header("Cache-Control", "no-cache, no-store, must-revalidate");
  // res.header("Pragma", "no-cache");
  // res.header("Expires", 0);
  // Get a Postgres client from the connection pool
  pg.connect(connectionString, (err, client, done) => {
    // Handle connection errors
    if(err) {
      done();
      console.log(err);
      return res.status(500).json({success: false, data: err});
    }
    // SQL Query > Select Data
    const query = client.query('SELECT * FROM masini ORDER BY id ASC;');
    // Stream results back one row at a time
    query.on('row', (row) => {
      masini.push(row);
    });

    // After all data is returned, close connection and return results
    query.on('end', () => {
      done();
      if(tok == req.headers.authorization.split(' ')[1])
        return res.json({ masini });
      else
        return res.status(401).json({success: false, data: 'Neautorizat'});
    });
  });
});

router.get('/api/masini/public', (req, res, next) => {
  const masini = [];
  // res.header("Cache-Control", "no-cache, no-store, must-revalidate");
  // res.header("Pragma", "no-cache");
  // res.header("Expires", 0);
  // Get a Postgres client from the connection pool
  pg.connect(connectionString, (err, client, done) => {
    // Handle connection errors
    if(err) {
      done();
      console.log(err);
      return res.status(500).json({success: false, data: err});
    }
    // SQL Query > Select Data
    const query = client.query('SELECT * FROM masini ORDER BY id ASC;');
    // Stream results back one row at a time
    query.on('row', (row) => {
      masini.push(row);
    });

    // After all data is returned, close connection and return results
    query.on('end', () => {
      done();
      return res.json({ masini });
    });
  });
});

module.exports = router;
