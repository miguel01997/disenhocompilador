package sintacticanalizer.ec.utils;

import java.util.List;
import sintacticanalizer.ec.common.WithId;

/**
 * Clase abstracta que implementa algunos metodos DAO.
 * Basado en Generics. Con control de Excepciones.
 * Carga el EMF de JpaResourceBean.
 * 
 * @author luiscampiz
 */
public abstract class AbstractJpaDao<T extends WithId> {

    
    public AbstractJpaDao() {

    }

    public void create(T entity) throws RuntimeException {
    }

    public void update(T entity) throws RuntimeException {
    }

    public void delete(T entity) throws RuntimeException {
    }

    
    public abstract T findById(Long id);

    
    public abstract List<T> getAll(String orderBy);
    
    public List<T> getAll() {
        return this.getAll(null);
    }
    
    public abstract List<T> getFromTo(int firstResult, int maxResults, String orderBy);
}

