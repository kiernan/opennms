package org.opennms.web.rest.config;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.opennms.core.config.api.ConfigurationResourceException;
import org.opennms.core.xml.AbstractJaxbConfigDao;
import org.opennms.netmgt.config.DataCollectionConfigDao;
import org.opennms.netmgt.config.datacollection.DatacollectionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.sun.jersey.api.core.ResourceContext;
import com.sun.jersey.spi.resource.PerRequest;

@Component
@PerRequest
@Scope("prototype")
public class DataCollectionConfigResource implements InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger(DataCollectionConfigResource.class);

    @Resource(name="dataCollectionConfigDao")
    private DataCollectionConfigDao m_dataCollectionConfigDao;

    @Context
    private ResourceContext m_context;

    @Context 
    private UriInfo m_uriInfo;

    public void setDataCollectionConfigDao(final DataCollectionConfigDao dao) {
        m_dataCollectionConfigDao = dao;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(m_dataCollectionConfigDao, "DataCollectionConfigDao must be set!");
        Assert.isTrue(m_dataCollectionConfigDao instanceof AbstractJaxbConfigDao<?,?>);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_ATOM_XML})
    public Response getDataCollectionConfiguration() throws ConfigurationResourceException {
        LOG.info("getDatacollectionConfigurationForLocation()");

        @SuppressWarnings("unchecked")
        final AbstractJaxbConfigDao<DatacollectionConfig,DatacollectionConfig> dao = (AbstractJaxbConfigDao<DatacollectionConfig,DatacollectionConfig>)m_dataCollectionConfigDao;
        final DatacollectionConfig dcc = dao.getContainer().getObject();
        if (dcc == null) {
            return Response.status(404).build();
        }

        return Response.ok(dcc.toDataCollectionConfig()).build();
    }
}