const pg = require('pg');
const connectionString = process.env.DATABASE_URL || 'postgres://localhost:5432/mobile-app';

const client = new pg.Client(connectionString);
client.connect();
const query = client.query(
  'CREATE TABLE utilizatori(id SERIAL PRIMARY KEY, nume VARCHAR(40) not null, parola VARCHAR(40) not null)');
query.on('end', () => { client.end(); });
