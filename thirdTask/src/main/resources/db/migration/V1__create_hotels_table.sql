-- Create hotels table
CREATE TABLE IF NOT EXISTS hotels (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    stars INTEGER CHECK (stars >= 1 AND stars <= 5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index on name for faster searches
CREATE INDEX idx_hotels_name ON hotels(name);

-- Insert some sample data
INSERT INTO hotels (name, description, stars) VALUES
    ('Grand Hotel', 'Роскошный отель в центре города с прекрасным видом', 5),
    ('Beach Resort', 'Отличный курортный отель на берегу моря', 4),
    ('City Inn', 'Уютный отель для деловых путешественников', 3);